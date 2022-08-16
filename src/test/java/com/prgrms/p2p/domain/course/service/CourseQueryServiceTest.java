package com.prgrms.p2p.domain.course.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.prgrms.p2p.domain.bookmark.repository.CourseBookmarkRepository;
import com.prgrms.p2p.domain.common.exception.NotFoundException;
import com.prgrms.p2p.domain.course.dto.DetailCourseResponse;
import com.prgrms.p2p.domain.course.dto.SummaryCourseResponse;
import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.course.entity.CoursePlace;
import com.prgrms.p2p.domain.course.entity.Period;
import com.prgrms.p2p.domain.course.entity.Region;
import com.prgrms.p2p.domain.course.entity.Spot;
import com.prgrms.p2p.domain.course.entity.Theme;
import com.prgrms.p2p.domain.course.repository.CourseRepository;
import com.prgrms.p2p.domain.like.repository.CourseLikeRepository;
import com.prgrms.p2p.domain.place.entity.Address;
import com.prgrms.p2p.domain.place.entity.Category;
import com.prgrms.p2p.domain.place.entity.PhoneNumber;
import com.prgrms.p2p.domain.place.entity.Place;
import com.prgrms.p2p.domain.user.entity.Sex;
import com.prgrms.p2p.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@ExtendWith(MockitoExtension.class)
@TestClassOrder(ClassOrderer.DisplayName.class)
class CourseQueryServiceTest {

  @InjectMocks
  CourseQueryService courseQueryService;

  @Mock
  CourseRepository courseRepository;

  @Mock
  CourseLikeRepository likeRepository;

  @Mock
  CourseBookmarkRepository bookmarkRepository;

  private Course course;
  private CoursePlace coursePlace1, coursePlace2;
  private User user;
  private Place place;
  private SliceImpl<Course> courses;

  @BeforeEach
  void setUp() {
    user = new User("dhkstnaos@gmail.com", "1234", "asdf", "1997-11-29", Sex.FEMALE);
    course = new Course("title", Period.ONE_DAY, Region.서울, Set.of(Theme.가족여행), Set.of(Spot.바다),
        user);
    place = new Place("1123", "제주", new Address("abc", "bbc"), "12.4", "32.4", Category.AC5,
        new PhoneNumber("010-1234-2345"));
    course.addCoursePlace(new CoursePlace(0, "aa", "s3...", true, false, course, place));
    course.addCoursePlace(new CoursePlace(1, "aa", "s3...", true, false, course, place));
    Pageable pageable = mock(Pageable.class);
    courses = new SliceImpl<>(List.of(course), pageable, false);
  }

  @Test
  @DisplayName("성공: 코스 목록 조회")
  void successFindSummary() {
    when(courseRepository.searchCourse(any(), any())).thenReturn(courses);
    Slice<SummaryCourseResponse> summaryList = courseQueryService.findSummaryList(any(), any(),
        10L);
    verify(courseRepository, times(1)).searchCourse(any(), any());

    Assertions.assertThat(summaryList.getContent().size()).isEqualTo(1);
    Assertions.assertThatObject(summaryList.getContent().get(0).getClass())
        .isEqualTo(SummaryCourseResponse.class);
  }

  @Nested
  @DisplayName("코스 상세 조회")
  class findDetail {

    @Test
    @DisplayName("성공: 코스 상세 조회")
    void successFindDetail() {
      when(courseRepository.findById(10L)).thenReturn(Optional.of(course));
      when(likeRepository.existsByUserIdAndCourse(any(Long.class), any(Course.class))).thenReturn(
          Boolean.TRUE);
      when(bookmarkRepository.existsByUserIdAndCourse(any(Long.class),
          any(Course.class))).thenReturn(Boolean.TRUE);
      DetailCourseResponse detail = courseQueryService.findDetail(10L, any(Long.class));

      verify(courseRepository, times(1)).findById(any(Long.class));
      verify(likeRepository, times(1)).existsByUserIdAndCourse(any(), any(Course.class));
      verify(bookmarkRepository, times(1)).existsByUserIdAndCourse(any(), any());
    }

    @Test
    @DisplayName("실패: 존재하지 않는 코스로 조회")
    void failAsNotFoundCourse() {
      when(courseRepository.findById(10L)).thenThrow(new NotFoundException("존재하지 않는 코스입니다."));
      Assertions.assertThatThrownBy(() -> courseQueryService.findDetail(10L, any(Long.class)))
          .isInstanceOf(NotFoundException.class);
      verify(courseRepository, times(1)).findById(any(Long.class));
    }
  }

  @Nested
  @DisplayName("유저가 북마크한 코스 조회")
  class findBookmarkedCourseList {

    @Test
    @DisplayName("성공: 북마크한 코스 조회")
    void successFindBookmarkedCourseList() {
      when(courseRepository.findBookmarkedCourse(any(Long.class), any())).thenReturn(courses);
      Slice<SummaryCourseResponse> summaryList = courseQueryService.findBookmarkedCourseList(any(),
          any(Long.class), any(Long.class));
      verify(courseRepository, times(1)).findBookmarkedCourse(any(Long.class), any());

      Assertions.assertThat(summaryList.getContent().size()).isEqualTo(1);
      Assertions.assertThatObject(summaryList.getContent().get(0).getClass())
          .isEqualTo(SummaryCourseResponse.class);
      Assertions.assertThat(summaryList.getContent().get(0).getIsBookmarked()).isTrue();
    }
  }

}