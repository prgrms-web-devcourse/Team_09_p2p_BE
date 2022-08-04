package com.prgrms.p2p.domain.like.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.prgrms.p2p.domain.common.exception.NotFoundException;
import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.course.entity.Period;
import com.prgrms.p2p.domain.course.entity.Region;
import com.prgrms.p2p.domain.course.entity.Spot;
import com.prgrms.p2p.domain.course.entity.Theme;
import com.prgrms.p2p.domain.course.repository.CourseRepository;
import com.prgrms.p2p.domain.like.entity.CourseLike;
import com.prgrms.p2p.domain.like.repository.CourseLikeRepository;
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
class CourseLikeServiceTest {

  @Mock
  private CourseLikeRepository courseLikeRepository;

  @Mock
  private CourseRepository courseRepository;

  @InjectMocks
  private CourseLikeService courseLikeService;

  @Nested
  @DisplayName("toggle() : 코스에 좋아요를 등록하거나 삭제합니다.")
  @TestMethodOrder(MethodOrderer.DisplayName.class)
  class ToggleTest {

    private Course courseStub;
    private CourseLike courseLikeStub;

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
      courseLikeStub = new CourseLike(1L, courseStub);
    }

    @Test
    @DisplayName("성공 : 존재하는 코스에 좋아요가 등록되었습니다.")
    void successLike() {
      // Given
      when(courseRepository.findById(any(Long.class))).thenReturn(Optional.of(courseStub));
      when(courseLikeRepository.findByUserIdAndCourse(any(Long.class),
          any(Course.class))).thenReturn(Optional.empty());
      when(courseLikeRepository.save(any(CourseLike.class))).thenReturn(courseLikeStub);
      // When
      courseLikeService.toggle(1L, 1L);
      // Then
      verify(courseRepository, times(1)).findById(any(Long.class));
      verify(courseLikeRepository, times(1)).findByUserIdAndCourse(any(Long.class),
          any(Course.class));
      verify(courseLikeRepository, times(1)).save(any(CourseLike.class));
    }

    @Test
    @DisplayName("성공 : 기존에 등록되어 있는 좋아요를 삭제합니다.")
    void successDislike() {
      // Given
      when(courseRepository.findById(any(Long.class))).thenReturn(Optional.of(courseStub));
      when(courseLikeRepository.findByUserIdAndCourse(any(Long.class), any(Course.class)))
          .thenReturn(Optional.of(courseLikeStub));
      doNothing().when(courseLikeRepository).delete(any(CourseLike.class));
      // When
      courseLikeService.toggle(1L, 1L);
      // Then
      verify(courseRepository, times(1)).findById(any(Long.class));
      verify(courseLikeRepository, times(1)).findByUserIdAndCourse(any(Long.class),
          any(Course.class));
      verify(courseLikeRepository, times(1)).delete(any(CourseLike.class));
    }

    @Test
    @DisplayName("실패 : 존재하지 않는 코스에 좋아요가 요청되었습니다.")
    void failLike() {
      // Given
      when(courseRepository.findById(any(Long.class))).thenReturn(Optional.empty());
      // When
      Throwable response = catchThrowable(() -> courseLikeService.toggle(1L, 1L));
      // Then
      verify(courseRepository, times(1)).findById(any(Long.class));
      assertThat(response).isInstanceOf(NotFoundException.class);
    }
  }
}