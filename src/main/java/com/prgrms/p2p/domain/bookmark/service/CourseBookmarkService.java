package com.prgrms.p2p.domain.bookmark.service;

import com.prgrms.p2p.domain.bookmark.entity.CourseBookmark;
import com.prgrms.p2p.domain.bookmark.repository.CourseBookmarkRepository;
import com.prgrms.p2p.domain.bookmark.service.BookmarkPolicy;
import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.course.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CourseBookmarkService implements BookmarkPolicy {

  private final CourseRepository courseRepository;
  private final CourseBookmarkRepository courseBookmarkRepository;

  @Override
  @Transactional
  public void toggle(Long userId, Long courseId) {
    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new RuntimeException("존재하지 않는 코스에 좋아요를 시도했습니다."));
    courseBookmarkRepository.findByUserIdAndCourse(userId, course)
        .ifPresentOrElse(this::dislike, () -> like(userId, course));
  }

  private void like(Long userId, Course course) {
    CourseBookmark courseBookmark = new CourseBookmark(userId, course);
    courseBookmarkRepository.save(courseBookmark);
  }

  private void dislike(CourseBookmark courseBookmark) {
    courseBookmark.deleteCourse(courseBookmark.getCourse());
    courseBookmarkRepository.delete(courseBookmark);
  }
}