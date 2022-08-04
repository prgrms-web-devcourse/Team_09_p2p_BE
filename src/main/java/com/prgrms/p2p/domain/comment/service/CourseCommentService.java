package com.prgrms.p2p.domain.comment.service;

import com.prgrms.p2p.domain.comment.dto.CourseCommentResponse;
import com.prgrms.p2p.domain.comment.dto.CreateCourseCommentRequest;
import com.prgrms.p2p.domain.comment.entity.CourseComment;
import com.prgrms.p2p.domain.comment.repository.CourseCommentRepository;
import com.prgrms.p2p.domain.comment.util.CommentConverter;
import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.course.repository.CourseRepository;
import com.prgrms.p2p.domain.user.repository.UserRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CourseCommentService {

  private final CourseCommentRepository courseCommentRepository;
  private final CourseRepository courseRepository;
  private final UserRepository userRepository;

  @Transactional
  public Long save(CreateCourseCommentRequest createCommentReq) {

    validateUser(createCommentReq.getUserId());

    Course course = courseRepository.findById(createCommentReq.getCourseId())
        .orElseThrow(() -> new RuntimeException("게시물이 존재하지 않습니다."));

    validateRootComment(createCommentReq.getRootCommentId());

    Long seq = getSeq(createCommentReq);

    CourseComment courseComment = CommentConverter.toCourseComment(createCommentReq, course, seq);

    return courseCommentRepository.save(courseComment).getId();
  }

  public Slice<CourseCommentResponse> findCourseComment(Long courseId, Pageable pageable) {

    return courseCommentRepository.findCourseComment(courseId, pageable);
  }

  private void validateUser(Long userId) {
    if (!userRepository.existsById(userId)) {
      throw new RuntimeException("사용자가 존재하지 않습니다.");
    }
  }

  private void validateRootComment(Long rootCommentId) {
    if (!Objects.isNull(rootCommentId) &&!courseCommentRepository.existsById(rootCommentId)) {
        throw new RuntimeException("존재하지 않는 댓글에 하위 댓글을 작성할 수 없습니다.");
    }
  }

  private Long getSeq(CreateCourseCommentRequest createCommentReq) {
    Long seq = Objects.isNull(createCommentReq.getRootCommentId()) ?
        0L : courseCommentRepository.findSequence(
        createCommentReq.getRootCommentId()) + 1;
    return seq;
  }
}