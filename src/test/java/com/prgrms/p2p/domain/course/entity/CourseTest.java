package com.prgrms.p2p.domain.course.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.prgrms.p2p.domain.common.exception.BadRequestException;
import com.prgrms.p2p.domain.user.entity.Sex;
import com.prgrms.p2p.domain.user.entity.User;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CourseTest {

  Course course;
  String title, description;
  Period period;
  Region region;
  Set<Theme> themes;
  Set<Spot> spots;
  User user;

  @BeforeEach
  void setup() {
    title = "title";
    description = "description";
    period = Period.ONE_DAY;
    region = Region.서울;
    themes = Set.of(Theme.가족여행);
    spots = Set.of(Spot.바다);
    user = new User("dhkstnaos@gmail.com", "1234", "asdf", "1997-11-29", Sex.FEMALE);
    course = new Course(title, period, region, themes, spots, user);
  }

  @Nested
  @DisplayName("Course 객체 생성")
  class create {

    @Test
    @DisplayName("성공: 생성 성공")
    public void createCourse() {
      //when
      //then
      assertThat(course.getTitle()).isEqualTo(title);
      assertThat(course.getThemes()).isNotEmpty();
    }

    @Test
    @DisplayName("성공: 테마나 포함장소가 비어있어도 생성")
    public void successAsThemesOrSpotsIsEmpty() {
      //when
      Course emptyCourse = new Course(title, period, region, new HashSet<>(),
          new HashSet<>(), user);
      //then
      assertThat(emptyCourse.getTitle()).isEqualTo(title);
      assertThat(emptyCourse.getThemes()).isEmpty();
    }

    @Test
    @DisplayName("실패: 타이틀이 비었을때.")
    public void failAsTitleOrDescriptionIsNull() {
      //when
      //then
      assertThatThrownBy(
          () -> new Course(null, period, region, new HashSet<>(), new HashSet<>(),
              user)).isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("실패: 유저 객체가 비었을때.")
    public void failAsUserIsNull() {
      //when
      //then
      assertThatThrownBy(
          () -> new Course(title, period, region, new HashSet<>(), new HashSet<>(),
              null)).isInstanceOf(BadRequestException.class);
    }
  }
}