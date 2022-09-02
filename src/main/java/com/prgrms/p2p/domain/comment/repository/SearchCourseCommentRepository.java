package com.prgrms.p2p.domain.comment.repository;

import com.prgrms.p2p.domain.comment.dto.CourseCommentForQueryDsl;
import com.prgrms.p2p.domain.course.entity.Course;
import java.util.List;

public interface SearchCourseCommentRepository {

  List<CourseCommentForQueryDsl> findCourseComment(Long CourseId);

  Long findSubCommentCount(Long commentId);

  List<CourseCommentForQueryDsl> findCourseCommentsByUserId(Long userId);

  Long countByUserId(Long userId);

  Long countByCourse(Course course);
}