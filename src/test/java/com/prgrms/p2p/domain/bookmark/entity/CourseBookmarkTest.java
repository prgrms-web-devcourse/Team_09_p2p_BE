package com.prgrms.p2p.domain.bookmark.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

import com.prgrms.p2p.domain.common.exception.BadRequestException;
import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.course.entity.Period;
import com.prgrms.p2p.domain.course.entity.Region;
import com.prgrms.p2p.domain.course.entity.Spot;
import com.prgrms.p2p.domain.course.entity.Theme;
import com.prgrms.p2p.domain.user.entity.Sex;
import com.prgrms.p2p.domain.user.entity.User;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.DisplayName.class)
class CourseBookmarkTest {

  private User user;
  private Set<Theme> themes;
  private Set<Spot> spots;

  @BeforeEach
  void setUp() {
    user = new User("email", "password", "nickName", LocalDate.now().toString(), Sex.FEMALE);
    themes = new HashSet<>();
    themes.add(Theme.데이트코스);
    spots = new HashSet<>();
    spots.add(Spot.바다);
  }

  @Test
  @DisplayName("성공 : 코스 북마크 객체를 생성합니다.")
  void success() {
    // Given
    Course course = new Course("title", Period.ONE_DAY, Region.서울, themes, spots,
        user);
    // When
    CourseBookmark courseBookmark = new CourseBookmark(1L, course);
    // Then
    assertThat(course.getCourseBookmarks()).contains(courseBookmark);
  }

  @Test
  @DisplayName("실패 : 유저 아이디가 빈값인 경우 예외를 반환합니다.")
  void failByUserId1() {
    // Given
    Course course = new Course("title", Period.ONE_DAY, Region.서울, themes, spots,
        user);
    // When
    Throwable response = catchThrowable(() -> new CourseBookmark(null, course));
    // Then
    assertThat(response).isInstanceOf(BadRequestException.class);
  }

  @Test
  @DisplayName("실패 : 유저 아이디가 0 이하인 경우 예외를 반환합니다.")
  void failByUserId2() {
    // Given
    Course course = new Course("title", Period.ONE_DAY, Region.서울, themes, spots,
        user);
    // When
    Throwable response = catchThrowable(() -> new CourseBookmark(0L, course));
    // Then
    assertThat(response).isInstanceOf(BadRequestException.class);
  }

  @Test
  @DisplayName("실패 : 코스가 빈값인 경우 예외를 반환합니다.")
  void failByCourse() {
    // When
    Throwable response = catchThrowable(() -> new CourseBookmark(1L, null));
    // Then
    assertThat(response).isInstanceOf(BadRequestException.class);
  }
}