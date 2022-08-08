package com.prgrms.p2p.domain.bookmark.service;

import com.prgrms.p2p.domain.bookmark.dto.BookmarkResponse;
import com.prgrms.p2p.domain.bookmark.entity.CourseBookmark;
import com.prgrms.p2p.domain.common.exception.NotFoundException;
import com.prgrms.p2p.domain.bookmark.repository.CourseBookmarkRepository;
import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.course.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CourseBookmarkService implements BookmarkService {

  private final CourseRepository courseRepository;
  private final CourseBookmarkRepository courseBookmarkRepository;

  @Override
  @Transactional
  public BookmarkResponse toggle(Long userId, Long courseId) {
    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new NotFoundException("존재하지 않는 코스에 북마크를 시도했습니다."));
    BookmarkResponse response = new BookmarkResponse();
    response.setId(courseId);
    courseBookmarkRepository.findByUserIdAndCourse(userId, course)
        .ifPresentOrElse(courseBookmark -> {
          unbookmark(courseBookmark);
          response.setIsBookmarked(Boolean.FALSE);
        }, () -> {
          bookmark(userId, course);
          response.setIsBookmarked(Boolean.TRUE);
        });
    return response;
  }

  @Override
  public Long countByUserId(Long userId) {
    return courseBookmarkRepository.countByUserId(userId);
  }

  private void bookmark(Long userId, Course course) {
    CourseBookmark courseBookmark = new CourseBookmark(userId, course);
    courseBookmarkRepository.save(courseBookmark);
  }

  private void unbookmark(CourseBookmark courseBookmark) {
    courseBookmark.deleteCourse(courseBookmark.getCourse());
    courseBookmarkRepository.delete(courseBookmark);
  }
}
