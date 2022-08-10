package com.prgrms.p2p.domain.course.service;

import static com.prgrms.p2p.domain.course.util.CourseConverter.ofSummary;

import com.prgrms.p2p.domain.bookmark.repository.CourseBookmarkRepository;
import com.prgrms.p2p.domain.common.exception.NotFoundException;
import com.prgrms.p2p.domain.course.dto.DetailCourseResponse;
import com.prgrms.p2p.domain.course.dto.SearchCourseRequest;
import com.prgrms.p2p.domain.course.dto.SummaryCourseResponse;
import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.course.repository.CourseRepository;
import com.prgrms.p2p.domain.course.util.CourseConverter;
import com.prgrms.p2p.domain.like.repository.CourseLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CourseQueryService {

  private final CourseRepository courseRepository;
  private final CourseLikeRepository likeRepository;
  private final CourseBookmarkRepository bookmarkRepository;

  public DetailCourseResponse findDetail(Long id, Long userId) {
    Course course = courseRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("존재하지 않는 코스입니다."));
    Boolean isLiked = likeRepository.existsByUserIdAndCourse(userId, course);
    Boolean isBookmarked = bookmarkRepository.existsByUserIdAndCourse(userId, course);
    return CourseConverter.ofDetail(course, isLiked, isBookmarked);
  }

  public Slice<SummaryCourseResponse> findSummaryList(SearchCourseRequest searchCourseRequest,
      Pageable pageable, Long userId) {
    Slice<Course> courses = courseRepository.searchCourse(searchCourseRequest, pageable);
    return courses.map(course -> ofSummary(course, course.getCourseBookmarks().stream()
        .anyMatch(courseBookmark -> courseBookmark.getUserId().equals(userId))));
  }

  public Slice<SummaryCourseResponse> findBookmarkedCourseList(Pageable pageable, Long userId) {
    Slice<Course> courses = courseRepository.findBookmarkedCourse(userId, pageable);
    return courses.map(course -> ofSummary(course, true));
  }

  public Long countByUserId(Long userId) {
    return courseRepository.countByUserId(userId);
  }

  public Slice<SummaryCourseResponse> findMyCourseList(Pageable pageable, Long userId) {
    Slice<Course> courses = courseRepository.findByUser_IdOrderByCreatedAtDesc(userId, pageable);
    return courses.map(course -> ofSummary(course, course.getCourseBookmarks().stream()
        .anyMatch(courseBookmark -> courseBookmark.getUserId().equals(userId))));
  }

  public Slice<SummaryCourseResponse> findOtherCourseList(Pageable pageable, Long userId,
      Long otherUserId) {
    Slice<Course> courses = courseRepository.findByUser_IdOrderByCreatedAtDesc(otherUserId,
        pageable);
    return courses.map(course -> ofSummary(course, course.getCourseBookmarks().stream()
        .anyMatch(courseBookmark -> courseBookmark.getUserId().equals(userId))));
  }
}
