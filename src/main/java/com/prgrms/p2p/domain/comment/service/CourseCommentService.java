package com.prgrms.p2p.domain.comment.service;

import static com.prgrms.p2p.domain.comment.util.CommentConverter.toCourseComment;

import com.prgrms.p2p.domain.comment.dto.CreateCommentRequest;
import com.prgrms.p2p.domain.comment.dto.UpdateCommentRequest;
import com.prgrms.p2p.domain.comment.entity.CourseComment;
import com.prgrms.p2p.domain.comment.entity.Visibility;
import com.prgrms.p2p.domain.comment.repository.CourseCommentRepository;
import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.course.repository.CourseRepository;
import com.prgrms.p2p.domain.user.repository.UserRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseCommentService {

  private final CourseCommentRepository courseCommentRepository;
  private final CourseRepository courseRepository;
  private final UserRepository userRepository;

  public Long save(CreateCommentRequest createCommentReq, Long courseId, Long userId) {

    validateAuth(!userRepository.existsById(userId), "존재하지 않는 계정입니다.");
    Course course = getCourse(courseId);

    Long rootCommentId = createCommentReq.getRootCommentId();
    validateAuth(
        !Objects.isNull(rootCommentId) &&
            !courseCommentRepository.existsById(rootCommentId),
        "존재하지 않는 댓글에 하위 댓글을 작성할 수 없습니다.");

    Long seq = getSeq(createCommentReq);

    CourseComment courseComment = toCourseComment(createCommentReq, course, seq, userId);

    return courseCommentRepository.save(courseComment).getId();
  }

  public Long updateCourseComment(
      UpdateCommentRequest updateReq,
      Long courseCommentId,
      Long courseId,
      Long userId) {

    validateAuth(!courseRepository.existsById(courseId), "게시글이 존재하지 않습니다.");
    CourseComment courseComment = courseCommentRepository.findById(courseCommentId)
        .orElseThrow(() -> new RuntimeException("수정 불가: 존재하지 않은 댓글입니다"));
    validateAuth(!courseComment.getUserId().equals(userId), "댓글의 수정 권한이 없습니다.");

    courseComment.changeComment(updateReq.getComment());
    return courseCommentId;
  }

  public void deleteCourseComment(Long courseCommentId, Long courseId, Long userId) {

    validateAuth(!courseRepository.existsById(courseId), "게시글이 존재하지 않습니다.");
    CourseComment courseComment = courseCommentRepository.findById(courseCommentId)
        .orElseThrow(() -> new RuntimeException("삭제 불가: 존재하지 않은 댓글입니다"));
    validateAuth(!courseComment.getUserId().equals(userId), "댓글의 삭제 권한이 없습니다.");

    if (!Objects.isNull(courseComment.getRootCommentId())) {
      deleteParentComment(courseComment);
      courseComment.changeVisibility(Visibility.FALSE);
      return;
    }

    if (courseCommentRepository.findSubCommentCount(courseComment.getId()) == 0) {
      courseComment.changeVisibility(Visibility.FALSE);
      return;
    }

    courseComment.changeVisibility(Visibility.DELETED_INFORMATION);
  }

  private Course getCourse(Long courseId) {
    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다. courseId:" + courseId));
    return course;
  }

  private void validateAuth(boolean courseComment, String message) {
    if (courseComment) {
      throw new RuntimeException(message);
    }
  }

  private Long getSeq(CreateCommentRequest createCommentReq) {
    Long seq = Objects.isNull(createCommentReq.getRootCommentId()) ?
        0L : courseCommentRepository.findSequence(
        createCommentReq.getRootCommentId()) + 1;
    return seq;
  }

  private void deleteParentComment(CourseComment courseComment) {
    if (courseCommentRepository.findSubCommentCount(courseComment.getRootCommentId()) == 1) {
      CourseComment parentComment
          = courseCommentRepository.findById(courseComment.getRootCommentId())
          .orElseThrow(RuntimeException::new);
      parentComment.changeVisibility(Visibility.FALSE);
    }
  }
}