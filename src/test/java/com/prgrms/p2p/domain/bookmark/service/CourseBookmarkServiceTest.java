package com.prgrms.p2p.domain.bookmark.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.prgrms.p2p.domain.bookmark.entity.CourseBookmark;
import com.prgrms.p2p.domain.bookmark.repository.CourseBookmarkRepository;
import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.course.entity.Period;
import com.prgrms.p2p.domain.course.entity.Region;
import com.prgrms.p2p.domain.course.entity.Spot;
import com.prgrms.p2p.domain.course.entity.Theme;
import com.prgrms.p2p.domain.course.repository.CourseRepository;
import com.prgrms.p2p.domain.user.entity.Sex;
import com.prgrms.p2p.domain.user.entity.User;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestClassOrder(ClassOrderer.DisplayName.class)
class CourseBookmarkServiceTest {

  @Mock
  private CourseRepository courseRepository;

  @Mock
  private CourseBookmarkRepository courseBookmarkRepository;

  @InjectMocks
  private CourseBookmarkService courseBookmarkService;

  @Nested
  @DisplayName("toggle() : 코스에 북마크를 등록하거나 삭제합니다.")
  @TestMethodOrder(MethodOrderer.DisplayName.class)
  class ToggleTest {

    private Course courseStub;
    private CourseBookmark courseBookmarkStub;

    @BeforeEach
    void setUp() {
      User userStub = new User("email", "password", "nickName",
          LocalDate.now().toString(), Sex.FEMALE);
      Set<Theme> themesStub = new HashSet<>();
      themesStub.add(Theme.데이트코스);
      Set<Spot> spots = new HashSet<>();
      spots.add(Spot.바다);
      courseStub = new Course("title", Period.ONE_DAY, Region.서울, "description",
          themesStub, spots, userStub);
      courseBookmarkStub = new CourseBookmark(1L, courseStub);
    }

    @Test
    @DisplayName("성공 : 존재하는 코스에 북마크가 등록되었습니다.")
    void successLike() {
      // Given
      when(courseRepository.findById(any(Long.class))).thenReturn(Optional.of(courseStub));
      when(courseBookmarkRepository.findByUserIdAndCourse(any(Long.class),
          any(Course.class))).thenReturn(Optional.empty());
      when(courseBookmarkRepository.save(any(CourseBookmark.class))).thenReturn(courseBookmarkStub);
      // When
      courseBookmarkService.toggle(1L, 1L);
      // Then
      verify(courseRepository, times(1)).findById(any(Long.class));
      verify(courseBookmarkRepository, times(1)).findByUserIdAndCourse(any(Long.class),
          any(Course.class));
      verify(courseBookmarkRepository, times(1)).save(any(CourseBookmark.class));
    }

    @Test
    @DisplayName("성공 : 기존에 등록되어 있는 북마크를 삭제합니다.")
    void successDislike() {
      // Given
      when(courseRepository.findById(any(Long.class))).thenReturn(Optional.of(courseStub));
      when(courseBookmarkRepository.findByUserIdAndCourse(any(Long.class), any(Course.class)))
          .thenReturn(Optional.of(courseBookmarkStub));
      doNothing().when(courseBookmarkRepository).delete(any(CourseBookmark.class));
      // When
      courseBookmarkService.toggle(1L, 1L);
      // Then
      verify(courseRepository, times(1)).findById(any(Long.class));
      verify(courseBookmarkRepository, times(1)).findByUserIdAndCourse(any(Long.class),
          any(Course.class));
      verify(courseBookmarkRepository, times(1)).delete(any(CourseBookmark.class));
    }

    @Test
    @DisplayName("실패 : 존재하지 않는 코스에 북마크가 요청되었습니다.")
    void failLike() {
      // Given
      when(courseRepository.findById(any(Long.class))).thenReturn(Optional.empty());
      // When
      Throwable response = catchThrowable(() -> courseBookmarkService.toggle(1L, 1L));
      // Then
      verify(courseRepository, times(1)).findById(any(Long.class));
      assertThat(response).isInstanceOf(RuntimeException.class);
    }
  }
}