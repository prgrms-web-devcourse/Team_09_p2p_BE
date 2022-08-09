package com.prgrms.p2p.domain.comment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.prgrms.p2p.domain.comment.dto.CreateCommentRequest;
import com.prgrms.p2p.domain.comment.dto.UpdateCommentRequest;
import com.prgrms.p2p.domain.comment.entity.CourseComment;
import com.prgrms.p2p.domain.comment.entity.PlaceComment;
import com.prgrms.p2p.domain.comment.repository.PlaceCommentRepository;
import com.prgrms.p2p.domain.common.exception.NotFoundException;
import com.prgrms.p2p.domain.common.exception.UnAuthorizedException;
import com.prgrms.p2p.domain.place.entity.Address;
import com.prgrms.p2p.domain.place.entity.Category;
import com.prgrms.p2p.domain.place.entity.PhoneNumber;
import com.prgrms.p2p.domain.place.entity.Place;
import com.prgrms.p2p.domain.place.repository.PlaceRepository;
import com.prgrms.p2p.domain.user.entity.Sex;
import com.prgrms.p2p.domain.user.entity.User;
import com.prgrms.p2p.domain.user.repository.UserRepository;
import java.time.LocalTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public class PlaceCommentServiceTest {

  @Autowired
  private PlaceCommentService placeCommentService;

  @Autowired
  private PlaceCommentRepository placeCommentRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PlaceRepository placeRepository;

  private Place place;
  private User user;

  private Long rootCommentId1;
  private Long rootCommentId2;
  private PlaceComment lastSubComment;
  @BeforeEach
  void setup() {

    //장소 생성
    String kakaoMapId = "kakaoMapId";
    String name = "name";
    String addressName = "addressName";
    String roadAddressName = "roadAddressName";
    Address address = new Address(addressName, roadAddressName);
    String latitude = "latitude";
    String logitude = "logitude";
    Category category = Category.MT1;
    PhoneNumber phoneNumber = new PhoneNumber("010-2345-5678");
    place = new Place(
        kakaoMapId,
        name,
        address,
        latitude,
        logitude,
        category,
        phoneNumber
    );

    //유저
    String email = "qjatlr@gmail.com";
    String password = "root1234!";
    String nick = "nickname";
    String birth = "2010-01-01";
    Sex male = Sex.FEMALE;

    user = userRepository.save(new User(email, password, nick, birth, male));
    Long userId = user.getId();

    Place savePlace = placeRepository.save(place);

    //댓글
    rootCommentId1 = placeCommentRepository.save(
        new PlaceComment("이것은 댓글1 입니다.", null, userId, place)).getId();

    rootCommentId2 = placeCommentRepository.save(
        new PlaceComment("이것은 댓글2 입니다.", null, userId, place)).getId();

    lastSubComment = placeCommentRepository.save(
        new PlaceComment("------> 4번의 첫번째 sub 댓글이에요. 작성시간 : " + LocalTime.now(),
            rootCommentId2, user.getId(), place));
  }

  @AfterEach
  void clear() {
    placeCommentRepository.deleteAll();
    placeRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Nested
  @DisplayName("장소 댓글 생성")
  class save {

    @Test
    @DisplayName("성공: 장소에 댓글을 작성")
    public void createRootComment() {

      String comment = "범석님 아프지마요.";
      Long rootCommentId = null;
      Long userId = user.getId();
      Long placeId = place.getId();

      CreateCommentRequest createCommentReq = CreateCommentRequest.builder()
          .comment(comment)
          .rootCommentId(rootCommentId)
          .build();

      Long placeCommentId = placeCommentService.save(createCommentReq, placeId, userId);
      PlaceComment placeComment = placeCommentRepository.findById(placeCommentId)
          .orElseThrow(RuntimeException::new);

      assertThat(placeComment.getComment()).isEqualTo(comment);
      assertThat(placeComment.getPlace()).isEqualTo(place);
      assertThat(placeComment.getRootCommentId()).isNull();
      assertThat(placeComment.getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("실패: 존재하지 않는 유저는 댓글을 작성할 수 없습니다.")
    public void failNotExistUser() {

      String comment = "범석님 아프지마요.";
      Long rootCommentId = null;
      Long userId = 100L;
      Long placeId = place.getId();

      CreateCommentRequest createCommentReq = CreateCommentRequest.builder()
          .comment(comment)
          .rootCommentId(rootCommentId)
          .build();

      assertThatThrownBy( () -> placeCommentService.save(createCommentReq, placeId, userId))
          .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("실패: 존재하지 않는 장소에는 댓글을 작성할 수 없습니다.")
    public void failNotExistPlace() {

      String comment = "범석님 아프지마요.";
      Long rootCommentId = null;
      Long userId = user.getId();
      Long placeId = 100L;

      CreateCommentRequest createCommentReq = CreateCommentRequest.builder()
          .comment(comment)
          .rootCommentId(rootCommentId)
          .build();

      assertThatThrownBy( () -> placeCommentService.save(createCommentReq, placeId, userId))
          .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("성공: 장소에 대댓글을 작성")
    public void createSubComment() {

      String comment = "범석님 아프지마요.";
      Long rootCommentId = rootCommentId1;
      Long userId = user.getId();
      Long placeId = place.getId();
      CreateCommentRequest createCommentReq = CreateCommentRequest.builder()
          .comment(comment)
          .rootCommentId(rootCommentId)
          .build();

      Long placeCommentId = placeCommentService.save(createCommentReq, placeId, userId);
      PlaceComment placeComment = placeCommentRepository.findById(placeCommentId)
          .orElseThrow(RuntimeException::new);

      assertThat(placeComment.getComment()).isEqualTo(comment);
      assertThat(placeComment.getPlace()).isEqualTo(place);
      assertThat(placeComment.getRootCommentId()).isEqualTo(rootCommentId1);
      assertThat(placeComment.getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("실패: 존재하지 않는 root 댓글에는 대댓글을 작성할 수 없습니다.")
    public void failNotExistRootComment() {

      String comment = "범석님 아프지마요.";
      Long rootCommentId = 100L;
      Long userId = user.getId();
      Long placeId = place.getId();

      CreateCommentRequest createCommentReq = CreateCommentRequest.builder()
          .comment(comment)
          .rootCommentId(rootCommentId)
          .build();

      assertThatThrownBy( () -> placeCommentService.save(createCommentReq, placeId, userId))
          .isInstanceOf(UnAuthorizedException.class);
    }
  }

  @Nested
  @DisplayName("장소 댓글 수정 기능 테스트")
  class updatePlaceComment {

    @Test
    @DisplayName("성공: 댓글을 정상적으로 수정합니다.")
    public void updateComment() {

      String newComment = "범석님 약드세요.";
      UpdateCommentRequest updateReq = UpdateCommentRequest.builder()
          .comment(newComment)
          .build();

      Long changedCommentId = placeCommentService.updatePlaceComment(
          updateReq, place.getId(), rootCommentId1, user.getId()
      );

      PlaceComment placeComment = placeCommentRepository.findById(changedCommentId)
          .orElseThrow(RuntimeException::new);

      assertThat(placeComment.getComment()).isEqualTo(newComment);
    }

    @Test
    @DisplayName("실패: 수정 권한이 없는 유저입니다.")
    public void failWrongUser() {

      String newComment = "범석님 약드세요.";
      Long userId = 100L;
      UpdateCommentRequest updateReq = UpdateCommentRequest.builder()
          .comment(newComment)
          .build();

      assertThatThrownBy(() -> placeCommentService
          .updatePlaceComment(updateReq, place.getId(), rootCommentId1, userId))
          .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("실패: 존재하지 않는 장소의 댓글은 수정할 수 없습니다.")
    public void failNotExistPlace() {

      String newComment = "범석님 약드세요.";
      Long placeId = 100L;
      UpdateCommentRequest updateReq = UpdateCommentRequest.builder()
          .comment(newComment)
          .build();

      assertThatThrownBy(() -> placeCommentService
          .updatePlaceComment(updateReq, placeId, rootCommentId1, user.getId()))
          .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("실패: 존재하지 않는 댓글은 수정할 수 없습니다.")
    public void failNotExistComment() {

      String newComment = "범석님 약드세요.";
      Long commentId = 100L;
      UpdateCommentRequest updateReq = UpdateCommentRequest.builder()
          .comment(newComment)
          .build();

      assertThatThrownBy(() -> placeCommentService
          .updatePlaceComment(updateReq, place.getId(), commentId, user.getId()))
          .isInstanceOf(NotFoundException.class);
    }
  }
}
