package com.prgrms.p2p.domain.comment.repository;

import com.prgrms.p2p.domain.comment.entity.CourseComment;
import com.prgrms.p2p.domain.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseCommentRepository extends JpaRepository<CourseComment, Long>,
    SearchCourseCommentRepository {
  Integer countByCourse(Course course);
}