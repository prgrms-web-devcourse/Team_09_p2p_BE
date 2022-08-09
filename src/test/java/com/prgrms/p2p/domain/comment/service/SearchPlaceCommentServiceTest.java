package com.prgrms.p2p.domain.comment.service;

import com.prgrms.p2p.domain.comment.entity.CourseComment;
import com.prgrms.p2p.domain.comment.entity.PlaceComment;
import com.prgrms.p2p.domain.comment.repository.PlaceCommentRepository;
import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.course.entity.Period;
import com.prgrms.p2p.domain.course.entity.Region;
import com.prgrms.p2p.domain.course.entity.Spot;
import com.prgrms.p2p.domain.course.entity.Theme;
import com.prgrms.p2p.domain.place.entity.Address;
import com.prgrms.p2p.domain.place.entity.Category;
import com.prgrms.p2p.domain.place.entity.PhoneNumber;
import com.prgrms.p2p.domain.place.entity.Place;
import com.prgrms.p2p.domain.place.repository.PlaceRepository;
import com.prgrms.p2p.domain.user.entity.Sex;
import com.prgrms.p2p.domain.user.entity.User;
import com.prgrms.p2p.domain.user.repository.UserRepository;
import java.time.LocalTime;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
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

    //유저
    String email = "qjatlr@gmail.com";
    String password = "root1234!";
    String nick = "nickname";
    String birth = "2010-01-01";
    Sex male = Sex.FEMALE;
    user = userRepository.save(new User(email, password, nick, birth, male));
    Long userId = user.getId();

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

    // parent comment 1
    parentComment1 = placeCommentRepository.save(
        new PlaceComment("parentComment1.                 작성시간 : " + LocalTime.now()
            , null, user.getId(), place));

    placeCommentRepository.save(
        new PlaceComment("------> 1번의 첫번째 sub 댓글이에요. 작성시간 : " + LocalTime.now(),
            parentComment1.getId(), user.getId(), place));

    // parent comment 2
    parentComment2 = placeCommentRepository.save(
        new PlaceComment("parentComment2.                 작성시간 : " + LocalTime.now(), null,
            user.getId(), place));

    middleSubComment = placeCommentRepository.save(
        new PlaceComment("------> 1번의 두번째 sub 댓글이에요. 작성시간 : " + LocalTime.now(),
            parentComment1.getId(), user.getId(), place));
    placeCommentRepository.save(
        new PlaceComment("------> 2번의 첫번째 sub 댓글이에요. 작성시간 : " + LocalTime.now(),
            parentComment2.getId(), user.getId(), place));

    placeCommentRepository.save(
        new PlaceComment("------> 2번의 두번째 sub 댓글이에요. 작성시간 : " + LocalTime.now(),
            parentComment2.getId(), user.getId(), place));
    placeCommentRepository.save(
        new PlaceComment("------> 2번의 세번째 sub 댓글이에요. 작성시간 : " + LocalTime.now(),
            parentComment2.getId(), user.getId(), place));
    placeCommentRepository.save(
        new PlaceComment("------> 1번의 세번째 sub 댓글이에요. 작성시간 : " + LocalTime.now(),
            parentComment1.getId(), user.getId(), place));

    // parent comment 3
    parentComment3 = placeCommentRepository.save(
        new PlaceComment("parentComment3.                 작성시간 : " + LocalTime.now(),
            null, user.getId(), place));

    // parent comment 4
    parentComment4 = placeCommentRepository.save(
        new PlaceComment("parentComment4.                 작성시간 : " + LocalTime.now(),
            null, user.getId(), place));

    lastSubComment = placeCommentRepository.save(
        new PlaceComment("------> 4번의 첫번째 sub 댓글이에요. 작성시간 : " + LocalTime.now(),
            parentComment4.getId(), user.getId(), place));
  }


}
