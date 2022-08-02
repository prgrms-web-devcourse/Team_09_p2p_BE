package com.prgrms.p2p.domain.comment.service;

import com.prgrms.p2p.domain.comment.dto.CreateCourseCommentRequest;
import com.prgrms.p2p.domain.comment.entity.CourseComment;
import com.prgrms.p2p.domain.comment.repository.CourseCommentRepository;
import com.prgrms.p2p.domain.comment.util.CommentConverter;
import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.course.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CourseCommentService {

  private final CourseCommentRepository courseCommentRepository;
  private final CourseRepository courseRepository;

  /**
   * 반환값은 코스 아이디입니다.
   */
  @Transactional
  public Long save(CreateCourseCommentRequest createCommentReq) {

    Course course = courseRepository.findById(createCommentReq.getCourseId())
        .orElseThrow(()->new RuntimeException("없는 게시물입니다."));

    Long seq = createCommentReq.getRootCommentId().isEmpty() ?
        0L : courseCommentRepository.findSequence(
        createCommentReq.getRootCommentId().get()) + 1;

    CourseComment courseComment = CommentConverter.toCourseComment(createCommentReq, course, seq);

    courseCommentRepository.save(courseComment);

    return createCommentReq.getCourseId();
  }
}