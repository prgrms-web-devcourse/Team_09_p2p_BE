package com.prgrms.p2p.domain.bookmark.repository;

import com.prgrms.p2p.domain.bookmark.entity.CourseBookmark;
import com.prgrms.p2p.domain.course.entity.Course;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseBookmarkRepository extends JpaRepository<CourseBookmark, Long> {

  Optional<CourseBookmark> findByUserIdAndCourse(Long userId, Course course);

  Boolean existsByUserIdAndCourse(Long userId, Course course);
}
