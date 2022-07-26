package com.prgrms.p2p.domain.comment.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.prgrms.p2p.domain.comment.dto.PlaceCommentDto;
import com.prgrms.p2p.domain.comment.dto.PlaceCommentResponse;
import com.prgrms.p2p.domain.comment.entity.PlaceComment;
import com.prgrms.p2p.domain.comment.repository.PlaceCommentRepository;
import com.prgrms.p2p.domain.place.entity.Address;
import com.prgrms.p2p.domain.place.entity.Category;
import com.prgrms.p2p.domain.place.entity.PhoneNumber;
import com.prgrms.p2p.domain.place.entity.Place;
import com.prgrms.p2p.domain.place.repository.PlaceRepository;
import com.prgrms.p2p.domain.user.entity.Sex;
import com.prgrms.p2p.domain.user.entity.User;
import com.prgrms.p2p.domain.user.repository.UserRepository;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public class SearchPlaceCommentServiceTest {

  @Autowired
  private SearchPlaceCommentService searchPlaceCommentService;

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

  private PlaceComment parentComment1;
  private PlaceComment parentComment2;
  private PlaceComment parentComment3;
  private PlaceComment parentComment4;
  private PlaceComment middleSubComment;
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

    place = placeRepository.save(
        new Place(
            kakaoMapId,
            name,
            address,
            latitude,
            logitude,
            category,
            phoneNumber
        )
    );

    //유저
    String email = "qjatlr@gmail.com";
    String password = "root1234!";
    String nickname = "nickname";
    String birth = "2010-01-01";
    Sex male = Sex.FEMALE;
    user = userRepository.save(new User(email, password, nickname, birth, male));
    Long userId = user.getId();

    // parent comment 1
    parentComment1 = placeCommentRepository.save(
        new PlaceComment("parentComment1.                 작성시간 : " + LocalTime.now()
            , null, userId, place));

    placeCommentRepository.save(
        new PlaceComment("------> 1번의 첫번째 sub 댓글이에요. 작성시간 : " + LocalTime.now(),
            parentComment1.getId(), userId, place));

    // parent comment 2
    parentComment2 = placeCommentRepository.save(
        new PlaceComment("parentComment2.                 작성시간 : " + LocalTime.now(), null,
            user.getId(), place));

    middleSubComment = placeCommentRepository.save(
        new PlaceComment("------> 1번의 두번째 sub 댓글이에요. 작성시간 : " + LocalTime.now(),
            parentComment1.getId(), userId, place));
    placeCommentRepository.save(
        new PlaceComment("------> 2번의 첫번째 sub 댓글이에요. 작성시간 : " + LocalTime.now(),
            parentComment2.getId(), userId, place));

    placeCommentRepository.save(
        new PlaceComment("------> 2번의 두번째 sub 댓글이에요. 작성시간 : " + LocalTime.now(),
            parentComment2.getId(), userId, place));
    placeCommentRepository.save(
        new PlaceComment("------> 2번의 세번째 sub 댓글이에요. 작성시간 : " + LocalTime.now(),
            parentComment2.getId(), userId, place));
    placeCommentRepository.save(
        new PlaceComment("------> 1번의 세번째 sub 댓글이에요. 작성시간 : " + LocalTime.now(),
            parentComment1.getId(), userId, place));

    // parent comment 3
    parentComment3 = placeCommentRepository.save(
        new PlaceComment("parentComment3.                 작성시간 : " + LocalTime.now(),
            null, userId, place));

    // parent comment 4
    parentComment4 = placeCommentRepository.save(
        new PlaceComment("parentComment4.                 작성시간 : " + LocalTime.now(),
            null, userId, place));

    lastSubComment = placeCommentRepository.save(
        new PlaceComment("------> 4번의 첫번째 sub 댓글이에요. 작성시간 : " + LocalTime.now(),
            parentComment4.getId(), userId, place));
  }

  @Test
  @DisplayName("순서에 맞게 댓글 전체 가져오기")
  public void getCorrectSequence() {

    // when
    PlaceCommentResponse response = searchPlaceCommentService.findPlaceComment(place.getId());

    for (int i = 0; i < response.getPlaceComments().size() - 1; i++) {

      if (Objects.isNull(response.getPlaceComments().get(i).getRootCommentId())) {

        if (Objects.isNull(response.getPlaceComments().get(i + 1).getRootCommentId())) {
          assertThat(response.getPlaceComments().get(i).getCreatedAt())
              .isBefore(response.getPlaceComments().get(i + 1).getCreatedAt());

        } else {
          assertThat(response.getPlaceComments().get(i).getId())
              .isEqualTo(response.getPlaceComments().get(i + 1).getRootCommentId());
          assertThat(response.getPlaceComments().get(i).getCreatedAt())
              .isBefore(response.getPlaceComments().get(i + 1).getCreatedAt());
        }

      } else {
        if (!Objects.isNull(response.getPlaceComments().get(i + 1).getRootCommentId())) {
          assertThat(response.getPlaceComments().get(i).getRootCommentId())
              .isEqualTo(response.getPlaceComments().get(i + 1).getRootCommentId());
          assertThat(response.getPlaceComments().get(i).getCreatedAt())
              .isBefore(response.getPlaceComments().get(i + 1).getCreatedAt());
        }
      }
    }
  }

  @Test
  @DisplayName("대댓글이 없는 댓글을 삭제하면 표기되지 않습니다.")
  public void deleteParentCommentNotExistSubComment() {

    PlaceCommentResponse responseBefore = searchPlaceCommentService.findPlaceComment(place.getId());
    List<PlaceCommentDto> beforeList = responseBefore.getPlaceComments().stream()
        .filter(bl -> bl.getId().equals(parentComment3.getId()))
        .collect(Collectors.toList());

    assertThat(beforeList.size()).isEqualTo(1);

    placeCommentService.deletePlaceComment(place.getId(),parentComment3.getId(), user.getId());

    PlaceCommentResponse responseAfter = searchPlaceCommentService.findPlaceComment(place.getId());
    List<PlaceCommentDto> afterList = responseAfter.getPlaceComments().stream()
        .filter(al -> al.getId().equals(parentComment3.getId()))
        .collect(Collectors.toList());

    assertThat(afterList.size()).isEqualTo(0);
  }

  @Test
  @DisplayName("대댓글이 있는 댓글을 칸은 남아 있지만 자세히 표기되지 않습니다.")
  public void deleteParentCommentExistSubComment() {

    PlaceCommentResponse responseBefore = searchPlaceCommentService.findPlaceComment(place.getId());
    List<PlaceCommentDto> beforeList = responseBefore.getPlaceComments().stream()
        .filter(bl -> bl.getId().equals(parentComment4.getId()))
        .collect(Collectors.toList());

    assertThat(beforeList.size()).isEqualTo(1);

    placeCommentService.deletePlaceComment(place.getId(), parentComment4.getId(), user.getId());

    PlaceCommentResponse responseAfter = searchPlaceCommentService.findPlaceComment(place.getId());
    List<PlaceCommentDto> afterList = responseAfter.getPlaceComments().stream()
        .filter(al -> al.getId().equals(parentComment4.getId()))
        .collect(Collectors.toList());

    assertThat(afterList.size()).isEqualTo(1);
    assertThat(afterList.get(0).getComment()).isEqualTo("삭제된 댓글입니다.");
    assertThat(afterList.get(0).getUser().getNickname()).isNull();
    assertThat(afterList.get(0).getUser().getProfileImage()).isNull();
    assertThat(afterList.get(0).getCreatedAt()).isNull();
    assertThat(afterList.get(0).getUpdatedAt()).isNull();
  }

  @Test
  @DisplayName("대댓글을 삭제할때 부모를 포함한 모두가 삭제된 상태라면 표기되지 않습니다.")
  public void deleteSubCommentNoOneNotExist() {

    PlaceCommentResponse responseBefore = searchPlaceCommentService.findPlaceComment(place.getId());
    List<PlaceCommentDto> beforeList = responseBefore.getPlaceComments().stream()
        .filter(bl -> bl.getId().equals(parentComment4.getId()))
        .collect(Collectors.toList());

    assertThat(beforeList.size()).isEqualTo(1);

    placeCommentService.deletePlaceComment(place.getId(), parentComment4.getId(), user.getId());
    placeCommentService.deletePlaceComment(place.getId(), lastSubComment.getId(), user.getId());

    PlaceCommentResponse responseAfter = searchPlaceCommentService.findPlaceComment(place.getId());
    List<PlaceCommentDto> afterList = responseAfter.getPlaceComments().stream()
        .filter(al -> al.getId().equals(parentComment4.getId()))
        .collect(Collectors.toList());

    assertThat(afterList.size()).isEqualTo(0);
  }

  @Test
  @DisplayName("대댓글은 바로 삭제할 수 있습니다.")
  public void deleteSubComment() {

    PlaceCommentResponse responseBefore = searchPlaceCommentService.findPlaceComment(place.getId());
    List<PlaceCommentDto> beforeList = responseBefore.getPlaceComments().stream()
        .filter(bl -> bl.getId().equals(middleSubComment.getId()))
        .collect(Collectors.toList());

    assertThat(beforeList.size()).isEqualTo(1);

    placeCommentService.deletePlaceComment(place.getId(), middleSubComment.getId(), user.getId());

    PlaceCommentResponse responseAfter = searchPlaceCommentService.findPlaceComment(place.getId());
    List<PlaceCommentDto> afterList = responseAfter.getPlaceComments().stream()
        .filter(al -> al.getId().equals(middleSubComment.getId()))
        .collect(Collectors.toList());

    assertThat(afterList.size()).isEqualTo(0);
  }
}
