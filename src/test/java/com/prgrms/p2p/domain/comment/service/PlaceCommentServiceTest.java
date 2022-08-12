package com.prgrms.p2p.domain.comment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.prgrms.p2p.domain.comment.dto.CreateCommentRequest;
import com.prgrms.p2p.domain.comment.dto.UpdateCommentRequest;
import com.prgrms.p2p.domain.comment.entity.PlaceComment;
import com.prgrms.p2p.domain.comment.entity.Visibility;
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
  private Place basicPlace;
  private User basicUser;


  private Long rootCommentId1;
  private Long rootCommentId2;
  private Long lastSubComment;

  private CreateCommentRequest createReq;
  private UpdateCommentRequest updateReq;
  private String comment;
  private String newComment;
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

    basicUser = userRepository.save(
        new User("e222@mail.com", "password222", "ban", "2009-01-01", Sex.FEMALE));

    basicPlace = placeRepository.save(
        new Place("kakaoMapId2",
            "또 다른 장소",
            new Address(addressName, roadAddressName),
            latitude,
            logitude,
            category,
            phoneNumber)
    );

    placeCommentRepository.save(
        new PlaceComment("basic place comment", null, basicUser.getId(), basicPlace));


    comment = "범석님 아프지마요.";
    newComment = "범석님 약드세요.";
    updateReq = UpdateCommentRequest.builder()
        .comment(newComment)
        .build();

    Place savePlace = placeRepository.save(place);

    //댓글
    rootCommentId1 = placeCommentRepository.save(
        new PlaceComment("이것은 댓글1 입니다.", null, userId, place)).getId();

    rootCommentId2 = placeCommentRepository.save(
        new PlaceComment("이것은 댓글2 입니다.", null, userId, place)).getId();

    lastSubComment = placeCommentRepository.save(
        new PlaceComment("첫번째 sub 댓글이에요.", rootCommentId2, user.getId(), place)).getId();
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

      Long rootCommentId = null;
      Long userId = user.getId();
      Long placeId = place.getId();

      createReq = CreateCommentRequest.builder()
          .comment(comment)
          .rootCommentId(rootCommentId)
          .build();

      Long placeCommentId = placeCommentService.save(createReq, placeId, userId);
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

      Long rootCommentId = null;
      Long userId = 100L;
      Long placeId = place.getId();

      createReq= CreateCommentRequest.builder()
          .comment(comment)
          .rootCommentId(rootCommentId)
          .build();

      assertThatThrownBy( () -> placeCommentService.save(createReq, placeId, userId))
          .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("실패: 존재하지 않는 장소에는 댓글을 작성할 수 없습니다.")
    public void failNotExistPlace() {

      Long rootCommentId = null;
      Long userId = user.getId();
      Long placeId = 100L;

      createReq = CreateCommentRequest.builder()
          .comment(comment)
          .rootCommentId(rootCommentId)
          .build();

      assertThatThrownBy( () -> placeCommentService.save(createReq, placeId, userId))
          .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("성공: 장소에 대댓글을 작성")
    public void createSubComment() {

      Long rootCommentId = rootCommentId1;
      Long userId = user.getId();
      Long placeId = place.getId();
      
      createReq = CreateCommentRequest.builder()
          .comment(comment)
          .rootCommentId(rootCommentId)
          .build();

      Long placeCommentId = placeCommentService.save(createReq, placeId, userId);
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

      Long rootCommentId = 100L;
      Long userId = user.getId();
      Long placeId = place.getId();

      createReq = CreateCommentRequest.builder()
          .comment(comment)
          .rootCommentId(rootCommentId)
          .build();

      assertThatThrownBy( () -> placeCommentService.save(createReq, placeId, userId))
          .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("실패: 대댓글에 대댓글을 작성하지 못합니다.")
    public void failCreateSubSubComment() {

      Long rootCommentId = lastSubComment;
      Long userId = user.getId();
      Long placeId = place.getId();

      createReq = CreateCommentRequest.builder()
          .comment(comment)
          .rootCommentId(rootCommentId)
          .build();

      assertThatThrownBy( () -> placeCommentService.save(createReq, placeId, userId))
          .isInstanceOf(RuntimeException.class);
    }
  }

  @Nested
  @DisplayName("장소 댓글 수정 기능 테스트")
  class updatePlaceComment {

    @Test
    @DisplayName("성공: 댓글을 정상적으로 수정합니다.")
    public void updateComment() {

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

      Long userId = 100L;

      assertThatThrownBy(() -> placeCommentService
          .updatePlaceComment(updateReq, place.getId(), rootCommentId1, userId))
          .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("실패: 존재하지 않는 장소의 댓글은 수정할 수 없습니다.")
    public void failNotExistPlace() {

      Long placeId = 100L;

      assertThatThrownBy(() -> placeCommentService
          .updatePlaceComment(updateReq, placeId, rootCommentId1, user.getId()))
          .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("실패: 존재하지 않는 댓글은 수정할 수 없습니다.")
    public void failNotExistComment() {

      Long commentId = 100L;

      assertThatThrownBy(() -> placeCommentService
          .updatePlaceComment(updateReq, place.getId(), commentId, user.getId()))
          .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("실패: 수정하려는 댓글이 그 코스에 존재하지 않습니다.")
    public void failNotMatchComment() {

      String newComment = "new Comment";
      UpdateCommentRequest updateReq = UpdateCommentRequest.builder()
          .comment(newComment)
          .build();

      Long commentId = basicPlace.getId();

      assertThatThrownBy(() -> placeCommentService.updatePlaceComment(
          updateReq, commentId, place.getId(), user.getId()))
          .isInstanceOf(RuntimeException.class);
    }
  }

  @Nested
  @DisplayName("댓글 삭제 테스트")
  class deletePlaceComment {

    @Test
    @DisplayName("성공: 대댓글이 없는 댓글을 삭제하면 visibility = false 입니다.")
    public void deleteNotHaveSubComment() {

      placeCommentService.deletePlaceComment(place.getId(),rootCommentId1, user.getId());

      PlaceComment placeComment = placeCommentRepository.findById(rootCommentId1)
          .orElseThrow(RuntimeException::new);

      assertThat(placeComment.getVisibility()).isEqualTo(Visibility.FALSE);
    }

    @Test
    @DisplayName("성공: 대댓글을 삭제하면 visibility = false 입니다.")
    public void deleteSubComment() {

      placeCommentService.deletePlaceComment(place.getId(),lastSubComment, user.getId());

      PlaceComment placeComment = placeCommentRepository.findById(lastSubComment)
          .orElseThrow(RuntimeException::new);

      assertThat(placeComment.getVisibility()).isEqualTo(Visibility.FALSE);
    }

    @Test
    @DisplayName("성공: 대댓글이 존재하는 댓글을 삭제하면 visibility = deleted_information 입니다.")
    public void deleteHaveSubComment() {

      placeCommentService.deletePlaceComment(place.getId(), rootCommentId2, user.getId());

      PlaceComment placeComment = placeCommentRepository.findById(rootCommentId2)
          .orElseThrow(RuntimeException::new);

      //then
      assertThat(placeComment.getVisibility()).isEqualTo(Visibility.DELETED_INFORMATION);
    }

    @Test
    @DisplayName("성공: 삭제된 댓글의 마지막 대댓글을 삭제하면(대댓글이 더이상 존재하지 않는다면), 댓글의 visibility = false 입니다")
    public void deleteNotHaveSubComment2() {

      placeCommentService.deletePlaceComment(place.getId(), rootCommentId2, user.getId());
      placeCommentService.deletePlaceComment(place.getId(), lastSubComment, user.getId());

      PlaceComment placeComment = placeCommentRepository.findById(rootCommentId2)
          .orElseThrow(RuntimeException::new);

      assertThat(placeComment.getVisibility()).isEqualTo(Visibility.FALSE);
    }

    @Test
    @DisplayName("실패: 삭제 권한이 없는 유저입니다.")
    public void failWrongUserId() {

      assertThatThrownBy(()->placeCommentService.deletePlaceComment(place.getId(), rootCommentId2, 100L))
          .isInstanceOf(UnAuthorizedException.class);
    }

    @Test
    @DisplayName("실패: 존재하지 않는 장소에 댓글은 삭제할 수 없습니다.")
    public void failWrongPlaceId() {

      assertThatThrownBy(()->placeCommentService.deletePlaceComment(100L, rootCommentId2, user.getId()))
          .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("실패: 존재하지 않는 댓글은 삭제할 수 없습니다.")
    public void failWrongCommentId() {

      assertThatThrownBy(()->placeCommentService.deletePlaceComment(place.getId(), 100L, user.getId()))
          .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("실패: 삭제하려는 댓글이 그 코스에 존재하지 않습니다.")
    public void failNotMatchComment() {

      String newComment = "new Comment";
      UpdateCommentRequest updateReq = UpdateCommentRequest.builder()
          .comment(newComment)
          .build();

      Long commentId = basicPlace.getId();

      assertThatThrownBy(() -> placeCommentService.deletePlaceComment(
          commentId, place.getId(), user.getId()))
          .isInstanceOf(RuntimeException.class);
    }
  }
}
