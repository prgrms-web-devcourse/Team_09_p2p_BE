package com.prgrms.p2p.domain.comment.service;

import static com.prgrms.p2p.domain.comment.util.CommentConverter.toCourseComment;
import static com.prgrms.p2p.domain.comment.util.CommentConverter.toCourseCommentResponse;

import com.prgrms.p2p.domain.comment.dto.CourseCommentDto;
import com.prgrms.p2p.domain.comment.dto.CourseCommentResponse;
import com.prgrms.p2p.domain.comment.dto.CreateCourseCommentRequest;
import com.prgrms.p2p.domain.comment.entity.CourseComment;
import com.prgrms.p2p.domain.comment.entity.Visibility;
import com.prgrms.p2p.domain.comment.repository.CourseCommentRepository;
import com.prgrms.p2p.domain.comment.util.CommentConverter;
import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.course.repository.CourseRepository;
import com.prgrms.p2p.domain.user.repository.UserRepository;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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
  public Long save(CreateCourseCommentRequest createCommentReq, Long courseId, Long userId) {

    validateUser(userId);

    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new RuntimeException("게시물이 존재하지 않습니다."));

    validateRootComment(createCommentReq.getRootCommentId());

    Long seq = getSeq(createCommentReq);

    CourseComment courseComment = toCourseComment(createCommentReq, course, seq, userId);

    return courseCommentRepository.save(courseComment).getId();
  }

  public List<CourseCommentResponse> findCourseComment(Long courseId) {

    List<CourseCommentDto> courseCommentDto = courseCommentRepository.findCourseComment(courseId);
    return courseCommentDto.stream()
        .map(CommentConverter::toCourseCommentResponse)
        .collect(Collectors.toList());
  }

  @Transactional
  public void deleteCourseComment(Long courseCommentId, Long courseId, Long userId) {

    CourseComment courseComment = courseCommentRepository.findById(courseCommentId)
        .orElseThrow(RuntimeException::new);

    if (!Objects.isNull(courseComment.getRootCommentId())) {
      if (courseCommentRepository.checkSubComment(courseComment.getRootCommentId()) == 1) {
        CourseComment parentComment = courseCommentRepository.findById(
                courseComment.getRootCommentId())
            .orElseThrow(RuntimeException::new);

        parentComment.changeVisibility(Visibility.FALSE);
      }
      courseComment.changeVisibility(Visibility.FALSE);
    }

    if (Objects.isNull(courseComment.getRootCommentId())) {
      if (courseCommentRepository.checkSubComment(courseComment.getId()) == 0) {
        courseComment.changeVisibility(Visibility.FALSE);
      } else {
        courseComment.changeVisibility(Visibility.DELETED_INFORMATION);
      }
    }
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