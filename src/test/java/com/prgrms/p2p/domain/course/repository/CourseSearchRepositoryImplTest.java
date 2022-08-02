package com.prgrms.p2p.domain.course.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.prgrms.p2p.domain.course.dto.SearchCourseRequest;
import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.course.entity.CoursePlace;
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
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Transactional
class CourseSearchRepositoryImplTest {

  @Autowired
  CourseRepository courseRepository;

  @Autowired
  PlaceRepository placeRepository;

  @Autowired
  CoursePlaceRepository coursePlaceRepository;

  @Autowired
  UserRepository userRepository;

  Course course;
  Place place;
  CoursePlace coursePlace, coursePlace2;
  String title, title2, description, kakaoMapId, name, addressName, roadAddressName, latitude, logitude;
  Address address;
  String number, imageUrl;
  PhoneNumber phoneNumber;
  Period period;
  Region region;
  Set<Theme> themes;
  Set<Spot> spots;
  User user;
  Category category;

  @BeforeEach
  void setup() {
    title = "제주 숙소";
    title2 = "부산 카페";
    description = "description";
    period = Period.ONE_DAY;
    region = Region.서울;
    user = new User("dhkstnaos@gmail.com", "1234", "asdf", "1997-11-29", Sex.FEMALE);
    //given
    kakaoMapId = "kakaoMapId";
    name = "name";
    addressName = "addressName";
    roadAddressName = "roadAddressName";
    address = new Address(addressName, roadAddressName);
    latitude = "latitude";
    logitude = "logitude";
    category = Category.MT1;
    number = "010-2345-5678";
    phoneNumber = new PhoneNumber(number);
    imageUrl = "s3..";
    place = new Place(kakaoMapId, name, address, latitude, logitude, category, phoneNumber);
    userRepository.save(user);
    placeRepository.save(place);
    for (int i = 0; i < 10; i++) {
      Course course;
      if (i % 2 == 0) {
        course = new Course(title, period, region, description, Set.of(Theme.가족여행, Theme.맛집),
            Set.of(Spot.바다, Spot.카페), user);
      } else {
        course = new Course(title2, period, region, description, Set.of(Theme.데이트코스, Theme.이쁜카페),
            Set.of(Spot.숙소, Spot.테마파크), user);
      }
      courseRepository.save(course);
      coursePlace = new CoursePlace(0, description, imageUrl, true, course, place);
      coursePlace2 = new CoursePlace(1, description, imageUrl, false, course, place);
      coursePlaceRepository.save(coursePlace);
      coursePlaceRepository.save(coursePlace2);
    }
  }

  @AfterEach
  void teardown() {
    courseRepository.deleteAll();
    placeRepository.deleteAll();
    coursePlaceRepository.deleteAll();
  }

  @Nested
  class keywordSearch {

    @Test
    @DisplayName("성공: 키워드 검색")
    public void successKeywordSearch() {
      SearchCourseRequest 제주 = new SearchCourseRequest("제주", null, null, null);
      PageRequest pageable = PageRequest.of(0, 10);
      Slice<Course> courses = courseRepository.searchCourse(제주, pageable);
      assertThat(courses.getNumberOfElements()).isEqualTo(5);
      assertThat(courses.getContent().get(0).getTitle()).contains("제주");
    }

    @Test
    @DisplayName("성공: 전체 검색")
    public void successWholeSearch() {
      SearchCourseRequest wholeKeyword = new SearchCourseRequest(null, null, null, null);
      PageRequest pageable = PageRequest.of(0, 10);
      Slice<Course> courses = courseRepository.searchCourse(wholeKeyword, pageable);
      assertThat(courses.getNumberOfElements()).isEqualTo(10);
    }

    @Test
    @DisplayName("성공: 존재하지 않는 키워드 검색")
    public void successEmptyKeywordSearch() {
      SearchCourseRequest nullKeyword = new SearchCourseRequest("null", null, null, null);
      PageRequest pageable = PageRequest.of(0, 10);
      Slice<Course> courses = courseRepository.searchCourse(nullKeyword, pageable);
      assertThat(courses.getNumberOfElements()).isEqualTo(0);
    }
  }

  @Nested
  class regionSearch {

    @Test
    @DisplayName("성공: 지역 검색")
    public void successExistRegionSearch() {
      SearchCourseRequest 서울 = new SearchCourseRequest(null, Region.서울, null, null);
      PageRequest pageable = PageRequest.of(0, 10);
      Slice<Course> courses = courseRepository.searchCourse(서울, pageable);
      assertThat(courses.getNumberOfElements()).isEqualTo(10);
    }

    @Test
    @DisplayName("성공: 없는 지역 검색")
    public void successNotExistRegionSearch() {
      SearchCourseRequest 경기 = new SearchCourseRequest(null, Region.경기, null, null);
      PageRequest pageable = PageRequest.of(0, 10);
      Slice<Course> courses = courseRepository.searchCourse(경기, pageable);
      assertThat(courses.getNumberOfElements()).isEqualTo(0);
    }

  }

  @Nested
  class spotSearch {

    @Test
    @DisplayName("성공: 포함장소 검색")
    public void successSportsSearch() {
      SearchCourseRequest 바다와숙소 = new SearchCourseRequest(null, Region.서울,
          List.of(Spot.바다, Spot.숙소), null);
      PageRequest pageable = PageRequest.of(0, 10);
      Slice<Course> courses = courseRepository.searchCourse(바다와숙소, pageable);
      assertThat(courses.getNumberOfElements()).isEqualTo(10);
    }

    @Test
    @DisplayName("성공: 전체 지역 검색")
    public void successNotExistRegionSearch() {
      SearchCourseRequest 전체지역 = new SearchCourseRequest(null, null, null, null);
      PageRequest pageable = PageRequest.of(0, 10);
      Slice<Course> courses = courseRepository.searchCourse(전체지역, pageable);
      assertThat(courses.getNumberOfElements()).isEqualTo(10);
    }

  }

  @Nested
  class themeSearch {

    @Test
    @DisplayName("성공: 테마 검색")
    public void successSportsSearch() {
      SearchCourseRequest 테마검색 = new SearchCourseRequest(null, Region.서울, null,
          List.of(Theme.가족여행, Theme.데이트코스,Theme.이쁜카페));
      PageRequest pageable = PageRequest.of(0, 20);
      Slice<Course> courses = courseRepository.searchCourse(테마검색, pageable);
      assertThat(courses.getNumberOfElements()).isEqualTo(10);
    }

    @Test
    @DisplayName("성공: 전체 테마 검색")
    public void successNotExistRegionSearch() {
      SearchCourseRequest 전체테마 = new SearchCourseRequest(null, null, null, null);
      PageRequest pageable = PageRequest.of(0, 20);
      Slice<Course> courses = courseRepository.searchCourse(전체테마, pageable);
      assertThat(courses.getNumberOfElements()).isEqualTo(10);
    }

  }
}