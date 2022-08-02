package com.prgrms.p2p.domain.course.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.prgrms.p2p.domain.place.entity.Address;
import com.prgrms.p2p.domain.place.entity.Category;
import com.prgrms.p2p.domain.place.entity.PhoneNumber;
import com.prgrms.p2p.domain.place.entity.Place;
import com.prgrms.p2p.domain.user.entity.Sex;
import com.prgrms.p2p.domain.user.entity.User;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CoursePlaceTest {

  Course course;
  Place place;
  CoursePlace coursePlace, coursePlace2;
  String title, description, kakaoMapId, name, addressName, roadAddressName, latitude, logitude;
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
  void setUp() {
  }

  @BeforeEach
  void setup() {
    title = "title";
    description = "description";
    period = Period.ONE_DAY;
    region = Region.서울;
    themes = Set.of(Theme.가족여행);
    spots = Set.of(Spot.바다);
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
    imageUrl = null;
    user = new User("dhkstnaos@gmail.com", "1234", "asdf", "1997-11-29", Sex.FEMALE);
    course = new Course(title, period, region, description, themes, spots, user);

    //when
    place = new Place(kakaoMapId, name, address, latitude, logitude, category, phoneNumber);
    coursePlace = new CoursePlace(0, description, imageUrl, true, course, place);
    coursePlace2 = new CoursePlace(1, description, imageUrl, false, course, place);
  }

  @Nested
  @DisplayName("CoursePlace 객체 생성")
  class createCoursePlace {

    @Test
    @DisplayName("성공: 생성 성공")
    public void createCoursePlace() {
      //when
      //then
      assertThat(coursePlace.getDescription()).isNotNull();
      assertThat(coursePlace.getRecommended()).isTrue();
    }

    @Test
    @DisplayName("실패: 인덱스나 설명이 비어있을때")
    public void failAsIndexOrDescriptionIsNull() {
      //when
      //then
      assertThatThrownBy(
          () -> new CoursePlace(0, null, imageUrl, true, course, place)).isInstanceOf(
          IllegalArgumentException.class);
    }

    @Test
    @DisplayName("실패: url이 비었을때.")
    public void failAsImageUrlIsNull() {
      //when
      //then
      assertThatThrownBy(
          () -> new CoursePlace(0, description, null, true, course, place)).isInstanceOf(
          IllegalArgumentException.class);
    }

    @Test
    @DisplayName("실패: 유저 객체가 비었을때.")
    public void failAsUserIsNull() {
      //when
      //then
      assertThatThrownBy(
          () -> new Course(title, period, region, description, new HashSet<>(), new HashSet<>(),
              null)).isInstanceOf(IllegalArgumentException.class);
    }
  }
}