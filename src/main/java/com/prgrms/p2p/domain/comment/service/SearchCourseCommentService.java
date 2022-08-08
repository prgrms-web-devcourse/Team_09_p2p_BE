package com.prgrms.p2p.domain.comment.service;

import static com.prgrms.p2p.domain.comment.util.CommentConverter.toCourseCommentResponse;

import com.prgrms.p2p.domain.comment.dto.CourseCommentForQueryDsl;
import com.prgrms.p2p.domain.comment.dto.CourseCommentResponse;
import com.prgrms.p2p.domain.comment.repository.CourseCommentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SearchCourseCommentService {

  private final CourseCommentRepository courseCommentRepository;

  public CourseCommentResponse findCourseComment(Long courseId) {

    List<CourseCommentForQueryDsl> commentForQueryDsl
        = courseCommentRepository.findCourseComment(courseId);

    return toCourseCommentResponse(commentForQueryDsl, courseId);
  }

  public Long countByUserId(Long userId) {
    return courseCommentRepository.countByUserId(userId);
  }
}