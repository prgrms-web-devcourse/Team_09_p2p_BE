package com.prgrms.p2p.domain.place.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.prgrms.p2p.domain.bookmark.entity.PlaceBookmark;
import com.prgrms.p2p.domain.bookmark.repository.PlaceBookmarkRepository;
import com.prgrms.p2p.domain.course.dto.CreateCoursePlaceRequest;
import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.course.entity.CoursePlace;
import com.prgrms.p2p.domain.course.entity.Period;
import com.prgrms.p2p.domain.course.entity.Region;
import com.prgrms.p2p.domain.course.entity.Spot;
import com.prgrms.p2p.domain.course.entity.Theme;
import com.prgrms.p2p.domain.course.repository.CoursePlaceRepository;
import com.prgrms.p2p.domain.course.repository.CourseRepository;
import com.prgrms.p2p.domain.like.entity.PlaceLike;
import com.prgrms.p2p.domain.like.repository.PlaceLikeRepository;
import com.prgrms.p2p.domain.place.dto.DetailPlaceResponse;
import com.prgrms.p2p.domain.place.dto.SearchPlaceRequest;
import com.prgrms.p2p.domain.place.dto.SummaryPlaceResponse;
import com.prgrms.p2p.domain.place.entity.Address;
import com.prgrms.p2p.domain.place.entity.Category;
import com.prgrms.p2p.domain.place.entity.PhoneNumber;
import com.prgrms.p2p.domain.place.entity.Place;
import com.prgrms.p2p.domain.place.repository.PlaceRepository;
import com.prgrms.p2p.domain.user.entity.Sex;
import com.prgrms.p2p.domain.user.entity.User;
import com.prgrms.p2p.domain.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class PlaceServiceTest {

  @Autowired
  PlaceService placeService;

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
    String courseDescription = "description";
    List<Spot> spotList = new ArrayList<>();
    spotList.add(Spot.바다);

    Course course = new Course(title, period, region, courseDescription, themes, null, user);
    courseRepository.save(course);

    //coursePlace
    int index = 0;
    String description = "description";
    String imageUrl = "/imageUrl";
    boolean recommended = false;
    CoursePlace coursePlace = new CoursePlace(index, description, imageUrl, recommended, course,
        savedPlace);

    CoursePlace savedCoursePlace = coursePlaceRepository.save(coursePlace);

  }

  Long placeId;
  Long userId;

  @AfterEach
  void deleteAll() {
    userRepository.deleteAll();
    courseRepository.deleteAll();
    coursePlaceRepository.deleteAll();
    placeRepository.deleteAll();
    placeLikeRepository.deleteAll();
    placeBookmarkRepository.deleteAll();
  }

  @Nested
  @DisplayName("저장")
  class save {

    @Test
    @DisplayName("성공: 장소 저장에 성공")
    public void savePlace() throws Exception {

      //given
      String kakaoMapId = "kakaoMapId";
      String name = "name";
      String addressName = "addressName";
      String roadAddressName = "roadAddressName";
      String latitude = "latitude";
      String longitude = "longitude";
      Category category = Category.MT1;
      String number = "010-1234-1234";
      PhoneNumber phoneNumber = new PhoneNumber(number);

      CreateCoursePlaceRequest createPlaceReq = CreateCoursePlaceRequest.builder()
          .kakaoMapId(kakaoMapId)
          .name(name)
          .addressName(addressName)
          .roadAddressName(roadAddressName)
          .latitude(latitude)
          .longitude(longitude)
          .category(category)
          .phoneNumber(phoneNumber)
          .build();

      //when
      Long placeId = placeService.save(createPlaceReq).getId();

      Place place = placeRepository.findById(placeId).orElseThrow(RuntimeException::new);

      //then
      assertThat(createPlaceReq).usingRecursiveComparison()
          .ignoringFields("addressName", "roadAddressName", "isRecommended", "description")
          .isEqualTo(place);

      assertThat(createPlaceReq.getAddressName()).isEqualTo(place.getAddress().getAddressName());
      assertThat(createPlaceReq.getRoadAddressName()).isEqualTo(
          place.getAddress().getRoadAddressName());
    }
  }

  @Nested
  @DisplayName("상세조회")
  class findDetail {

    @Test
    @DisplayName("성공: 상세조회 성공, 비로그인 유저")
    public void findDetail() throws Exception {

      //given
      Place place = placeRepository.findById(placeId).orElseThrow(RuntimeException::new);

      //when
      DetailPlaceResponse detailPlaceResp = placeService.findDetail(placeId, Optional.empty());

      //then
      assertThat(detailPlaceResp).usingRecursiveComparison()
          .ignoringFields("likeCount", "usedCount", "phone", "addressName", "roadAddressName",
              "liked", "bookmarked", "imageUrl").isEqualTo(place);

      assertThat(detailPlaceResp.getAddressName()).isEqualTo(place.getAddress().getAddressName());
      assertThat(detailPlaceResp.getRoadAddressName()).isEqualTo(
          place.getAddress().getRoadAddressName());

      assertThat(detailPlaceResp.getPhoneNumber().getNumber()).isEqualTo(
          place.getPhoneNumber().getNumber());

      assertThat(detailPlaceResp.getLiked()).isFalse();
      assertThat(detailPlaceResp.getBookmarked()).isFalse();

      assertThat(detailPlaceResp.getUsedCount()).isEqualTo(1);
      assertThat(detailPlaceResp.getLikeCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("성공: 상세조회 성공, 로그인 유저, 좋아요 안했음, 북마크 안했음")
    public void findDetailByLoginUser() throws Exception {

      //given
      Place place = placeRepository.findById(placeId).orElseThrow(RuntimeException::new);

      //when
      DetailPlaceResponse detailPlaceResp = placeService.findDetail(placeId,
          Optional.ofNullable(userId));

      //then
      assertThat(detailPlaceResp).usingRecursiveComparison()
          .ignoringFields("likeCount", "usedCount", "phone", "addressName", "roadAddressName",
              "liked", "bookmarked", "imageUrl").isEqualTo(place);

      assertThat(detailPlaceResp.getAddressName()).isEqualTo(place.getAddress().getAddressName());
      assertThat(detailPlaceResp.getRoadAddressName()).isEqualTo(
          place.getAddress().getRoadAddressName());

      assertThat(detailPlaceResp.getPhoneNumber().getNumber()).isEqualTo(
          place.getPhoneNumber().getNumber());

      assertThat(detailPlaceResp.getLiked()).isFalse();
      assertThat(detailPlaceResp.getBookmarked()).isFalse();

      assertThat(detailPlaceResp.getUsedCount()).isEqualTo(1);
      assertThat(detailPlaceResp.getLikeCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("성공: 상세조회 성공, 로그인 유저(좋아요 누른 기록 있음) -> liked = True, likeCount 증가")
    public void findDetailByLoginUserExpressLike() throws Exception {

      //given
      Place place = placeRepository.findById(placeId).orElseThrow(RuntimeException::new);
      PlaceLike placeLike = new PlaceLike(userId, place);
      placeLikeRepository.save(placeLike);

      //when
      DetailPlaceResponse detailPlaceResp = placeService.findDetail(placeId,
          Optional.ofNullable(userId));

      //then
      assertThat(detailPlaceResp).usingRecursiveComparison()
          .ignoringFields("likeCount", "usedCount", "phone", "addressName", "roadAddressName",
              "liked", "bookmarked", "imageUrl").isEqualTo(place);

      assertThat(detailPlaceResp.getLiked()).isTrue();
      assertThat(detailPlaceResp.getBookmarked()).isFalse();

      assertThat(detailPlaceResp.getUsedCount()).isEqualTo(1);
      assertThat(detailPlaceResp.getLikeCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("성공: 상세조회 성공, 로그인 유저, 북마크는 했음")
    public void findDetailByLoginUserBookmark() throws Exception {

      //given
      Place place = placeRepository.findById(placeId).orElseThrow(RuntimeException::new);
      PlaceBookmark placeBookmark = new PlaceBookmark(userId, place);
      placeBookmarkRepository.save(placeBookmark);

      //when
      DetailPlaceResponse detailPlaceResp = placeService.findDetail(placeId,
          Optional.ofNullable(userId));

      //then
      assertThat(detailPlaceResp.getLiked()).isFalse();
      assertThat(detailPlaceResp.getBookmarked()).isTrue();

      assertThat(detailPlaceResp.getUsedCount()).isEqualTo(1);
      assertThat(detailPlaceResp.getLikeCount()).isEqualTo(0);
    }
  }

  @Nested
  @DisplayName("요약 리스트 조회")
  class findSummaryList {

    @Test
    @DisplayName("성공: 요약 리스트 조회 성공, 비로그인")
    public void findSummaryList() throws Exception {

      //given
      String keyword = "na";

      SearchPlaceRequest searchPlaceReq = SearchPlaceRequest.builder()
          .keyword(keyword)
          .userId(null)
          .build();

      Pageable pageable = PageRequest.of(0, 10);

      //when
      Slice<SummaryPlaceResponse> summaryList = placeService.findSummaryList(searchPlaceReq,
          pageable);

      //then
      for (SummaryPlaceResponse summaryPlaceResponse : summaryList) {
        assertThat(summaryPlaceResponse.getTitle()).contains(keyword);
        assertThat(summaryPlaceResponse.getBookmarked()).isFalse();
      }
    }

    @Test
    @DisplayName("성공: 요약 리스트 조회 성공, 로그인 북마크 안했음")
    public void findSummaryListByLoginUserNonBookmark() throws Exception {

      //given
      String keyword = "";

      SearchPlaceRequest searchPlaceReq = SearchPlaceRequest.builder()
          .keyword(keyword)
          .userId(userId)
          .build();

      Pageable pageable = PageRequest.of(0, 10);

      //when
      Slice<SummaryPlaceResponse> summaryList
          = placeService.findSummaryList(searchPlaceReq, pageable);

      //then
      for (SummaryPlaceResponse summaryPlaceResponse : summaryList) {
        assertThat(summaryPlaceResponse.getTitle()).contains(keyword);
        assertThat(summaryPlaceResponse.getBookmarked()).isFalse();
      }
    }

    @Test
    @DisplayName("성공: 요약 리스트 조회 성공, 로그인 북마크 했음")
    public void findSummaryListByLoginAndBookmarkUser() throws Exception {

      //given
      Place place = placeRepository.findById(placeId).orElseThrow(RuntimeException::new);
      PlaceBookmark placeBookmark = new PlaceBookmark(userId, place);
      placeBookmarkRepository.save(placeBookmark);

      String keyword = "";

      SearchPlaceRequest searchPlaceReq = SearchPlaceRequest.builder()
          .keyword(keyword)
          .userId(userId)
          .build();

      Pageable pageable = PageRequest.of(0, 10);

      //when
      Slice<SummaryPlaceResponse> summaryList
          = placeService.findSummaryList(searchPlaceReq, pageable);

      //then
      for (SummaryPlaceResponse summaryPlaceResponse : summaryList) {
        assertThat(summaryPlaceResponse.getTitle()).contains(keyword);
        assertThat(summaryPlaceResponse.getBookmarked()).isTrue();
      }
    }
  }

  @Nested
  @DisplayName("특정 유저의 북마크한 장소 목록 조회")
  class bookmarkedPlaceByUserId {

    @Test
    @DisplayName("성공: 북마크 하나도 없어서 조회 개수 0")
    public void findBookmarkedPlaceList() throws Exception {

      //given
      Pageable pageable = PageRequest.of(0, 10);

      //when
      Slice<SummaryPlaceResponse> bookmarkedPlaceList
          = placeService.findBookmarkedPlaceList(userId, pageable);

      //then
      assertThat(bookmarkedPlaceList.getNumberOfElements()).isEqualTo(0);
    }

    @Test
    @DisplayName("성공: 북마크 하나도 없어서 조회 개수 1")
    public void findBookmarkedPlaceListResult1() throws Exception {

      //given
      Place place = placeRepository.findById(placeId).orElseThrow(RuntimeException::new);
      PlaceBookmark placeBookmark = new PlaceBookmark(userId, place);
      placeBookmarkRepository.save(placeBookmark);

      Pageable pageable = PageRequest.of(0, 10);

      //when
      Slice<SummaryPlaceResponse> bookmarkedPlaceList
          = placeService.findBookmarkedPlaceList(userId, pageable);

      //then
      assertThat(bookmarkedPlaceList.getNumberOfElements()).isEqualTo(1);
    }
  }
}