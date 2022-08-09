package com.prgrms.p2p.domain.place.repository;

import static com.prgrms.p2p.domain.place.util.PlaceConverter.toSearchPlaceDto;
import static org.assertj.core.api.Assertions.assertThat;

import com.prgrms.p2p.domain.bookmark.repository.PlaceBookmarkRepository;
import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.course.entity.CoursePlace;
import com.prgrms.p2p.domain.course.entity.Period;
import com.prgrms.p2p.domain.course.entity.Region;
import com.prgrms.p2p.domain.course.entity.Sorting;
import com.prgrms.p2p.domain.course.entity.Spot;
import com.prgrms.p2p.domain.course.entity.Theme;
import com.prgrms.p2p.domain.course.repository.CoursePlaceRepository;
import com.prgrms.p2p.domain.course.repository.CourseRepository;
import com.prgrms.p2p.domain.like.repository.PlaceLikeRepository;
import com.prgrms.p2p.domain.place.dto.SearchPlaceDto;
import com.prgrms.p2p.domain.place.entity.Address;
import com.prgrms.p2p.domain.place.entity.Category;
import com.prgrms.p2p.domain.place.entity.PhoneNumber;
import com.prgrms.p2p.domain.place.entity.Place;
import com.prgrms.p2p.domain.user.entity.Sex;
import com.prgrms.p2p.domain.user.entity.User;
import com.prgrms.p2p.domain.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class PlaceSearchRepositoryTest {

  @Autowired
  PlaceRepository placeRepository;

  @Autowired
  CoursePlaceRepository coursePlaceRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  CourseRepository courseRepository;

  @Autowired
  PlaceLikeRepository placeLikeRepository;

  @Autowired
  PlaceBookmarkRepository placeBookmarkRepository;

  @BeforeEach
  void setup() {

    //place
    String kakaoMapId = "kakaoMapId";
    String name = "name";
    String addressName = "addressName";
    String roadAddressName = "roadAddressName";
    String latitude = "latitude";
    String longitude = "longitude";
    Category category = Category.MT1;
    String number = "010-114-1234";
    PhoneNumber phoneNumber = new PhoneNumber(number);

    Place place = new Place(kakaoMapId, name, new Address(addressName, roadAddressName), latitude,
        longitude, category, phoneNumber);
    Place savedPlace = placeRepository.save(place);
    placeId = savedPlace.getId();

    //user
    String email = "e@mail.com";
    String password = "password";
    String nickname = "nick";
    String birth = "2020-02-02";
    Sex male = Sex.MALE;

    User user = new User(email, password, nickname, birth, male);
    User savedUser = userRepository.save(user);
    userId = savedUser.getId();

    //course
    Set<Theme> themes = new HashSet<>();
    themes.add(Theme.가족여행);
    String title = "title";
    Period period = Period.ONE_DAY;
    Region region = Region.서울;
    List<Spot> spotList = new ArrayList<>();
    spotList.add(Spot.바다);

    Course course = new Course(title, period, region, themes, null, user);
    courseRepository.save(course);

    //coursePlace
    int index = 0;
    String description = "description";
    String imageUrl = "/imageUrl";
    boolean recommended = false;
    CoursePlace coursePlace = new CoursePlace(index, description, imageUrl, recommended, false,
        course, savedPlace);

    CoursePlace savedCoursePlace = coursePlaceRepository.save(coursePlace);
  }

  Long placeId;
  Long userId;

  @Test
  @DisplayName("성공: 장소로 요약 리스트 조회 성공")
  public void findSummaryListByRegion() throws Exception {

    //given
    Optional<String> keyword = Optional.empty();
    Optional<Sorting> orderByFamous = Optional.empty();
    Optional<Region> region = Optional.ofNullable(Region.서울);
    SearchPlaceDto searchPlaceDto = toSearchPlaceDto(keyword, region, orderByFamous);

    Pageable pageable = PageRequest.of(0, 100);

    //when
    Slice<Place> places = placeRepository.searchPlace(searchPlaceDto, pageable);

    //then
    for (Place place : places) {
      place.getCoursePlaces().stream().map(
          cp -> assertThat(cp.getCourse().getRegion()).isEqualTo(region.get())
      );
    }
  }

  @Test
  @DisplayName("kakaoMapId = null 인 장소는 조회되지 않습니다.")
  public void invisiblePlacesThatKakaoMapIdIsNull() throws Exception {

    //given
    String kakaoMapId = null;
    String name = "name";
    String addressName = "addressName";
    String roadAddressName = "roadAddressName";
    String latitude = "latitude";
    String longitude = "longitude";
    Category category = Category.MT1;
    String number = "010-1234-1234";
    PhoneNumber phoneNumber = new PhoneNumber(number);

    Place place = new Place(kakaoMapId, name, new Address(addressName, roadAddressName), latitude,
        longitude, category, phoneNumber);
    Place savedPlace = placeRepository.save(place);

    //when
    Optional<String> keyword = Optional.empty();
    Optional<Sorting> orderByFamous = Optional.empty();
    Optional<Region> region = Optional.empty();
    SearchPlaceDto searchPlaceDto = toSearchPlaceDto(keyword, region, orderByFamous);

    Pageable pageable = PageRequest.of(0, 100);

    Slice<Place> places = placeRepository.searchPlace(searchPlaceDto, pageable);

    //then
    for (Place p : places) {
      assertThat(p.getKakaoMapId()).isNotNull();
    }
  }
}
