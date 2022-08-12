package com.prgrms.p2p.domain.comment.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.prgrms.p2p.domain.comment.dto.PlaceCommentForQueryDsl;
import com.prgrms.p2p.domain.comment.entity.PlaceComment;
import com.prgrms.p2p.domain.place.entity.Address;
import com.prgrms.p2p.domain.place.entity.Category;
import com.prgrms.p2p.domain.place.entity.PhoneNumber;
import com.prgrms.p2p.domain.place.entity.Place;
import com.prgrms.p2p.domain.place.repository.PlaceRepository;
import com.prgrms.p2p.domain.user.entity.Sex;
import com.prgrms.p2p.domain.user.entity.User;
import com.prgrms.p2p.domain.user.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
public class SearchPlaceCommentRepositoryImplTest {

  @Autowired
  private PlaceCommentRepository placeCommentRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PlaceRepository placeRepository;

  private Place place;
  private User user;

  @BeforeEach
  void setup() {
    //장소 생성
    String kakaoMapId = "kakaoMapId";
    String name = "qjatjr";
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
    Long rootCommentId = placeCommentRepository.save(
        new PlaceComment("이것은 루트 댓글1 입니다.", null, userId, place)).getId();
    placeCommentRepository.save(
        new PlaceComment("이것은 1번에 대댓글1 입니다.", rootCommentId, userId, place)).getId();
    placeCommentRepository.save(
        new PlaceComment("이것은 1번에 대댓글2 입니다.", rootCommentId, userId, place)).getId();

    Long rootCommentId2 = placeCommentRepository.save(
        new PlaceComment("이것은 루트 댓글2 입니다.", null, userId, place)).getId();
    placeCommentRepository.save(
        new PlaceComment("이것은 2번에 대댓글1 입니다.", rootCommentId2, userId, place)).getId();
    placeCommentRepository.save(
        new PlaceComment("이것은 2번에 대댓글2 입니다.", rootCommentId2, userId, place)).getId();

    placeCommentRepository.save(
        new PlaceComment("이것은 1번에 대댓글3 입니다.", rootCommentId, userId, place)).getId();
    placeCommentRepository.save(
        new PlaceComment("이것은 1번에 대댓글4 입니다.", rootCommentId, userId, place)).getId();

  }

  @AfterEach
  void clear() {
    placeCommentRepository.deleteAll();
    placeRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  @DisplayName("장소에 관련된 댓글 전체를 가져옵니다.")
  public void findPlaceComments() {

    List<PlaceCommentForQueryDsl> comments = placeCommentRepository.findPlaceComments(place.getId());
    assertThat(comments.size()).isEqualTo(8);
  }

  @Test
  @DisplayName("입력한 장소에 댓글이 존재하지 않는 경우 빈 리스트로 반환됩니다.")
  public void findPlaceCommentsEmpty() {

    List<PlaceCommentForQueryDsl> comments = placeCommentRepository.findPlaceComments(100L);
    assertThat(comments.size()).isEqualTo(0);
  }
}
