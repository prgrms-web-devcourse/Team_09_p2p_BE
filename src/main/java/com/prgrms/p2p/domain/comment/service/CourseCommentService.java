package com.prgrms.p2p.domain.comment.service;

import static com.prgrms.p2p.domain.comment.entity.Visibility.DELETED_INFORMATION;
import static com.prgrms.p2p.domain.comment.entity.Visibility.FALSE;
import static com.prgrms.p2p.domain.comment.util.CommentConverter.toCourseComment;

import com.prgrms.p2p.domain.comment.dto.CreateCommentRequest;
import com.prgrms.p2p.domain.comment.dto.UpdateCommentRequest;
import com.prgrms.p2p.domain.comment.entity.CourseComment;
import com.prgrms.p2p.domain.comment.repository.CourseCommentRepository;
import com.prgrms.p2p.domain.common.exception.BadRequestException;
import com.prgrms.p2p.domain.common.exception.NotFoundException;
import com.prgrms.p2p.domain.common.exception.UnAuthorizedException;
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

    if (!Objects.isNull(rootCommentId)) {
      CourseComment parentComment = courseCommentRepository.findById(rootCommentId)
          .orElseThrow(() -> new NotFoundException("존재하지 않는 댓글에 하위 댓글을 작성할 수 없습니다."));
      if (!Objects.isNull(parentComment.getRootCommentId())) {
        throw new BadRequestException("대댓글에 대댓글을 작성할 수 없습니다.");
      }
    }

    CourseComment courseComment = toCourseComment(createCommentReq, course, userId);

    return courseCommentRepository.save(courseComment).getId();
  }

  public Long updateCourseComment(UpdateCommentRequest updateReq, Long courseCommentId,
      Long courseId, Long userId) {

    validateAuth(!courseRepository.existsById(courseId), "게시글이 존재하지 않습니다.");
    CourseComment courseComment = courseCommentRepository.findById(courseCommentId)
        .orElseThrow(() -> new NotFoundException("수정 불가: 존재하지 않은 댓글입니다"));
    validateAuth(!courseComment.getUserId().equals(userId), "댓글의 수정 권한이 없습니다.");

    courseComment.changeComment(updateReq.getComment());
    return courseCommentId;
  }

  public void deleteCourseComment(Long courseCommentId, Long courseId, Long userId) {

    validateAuth(!courseRepository.existsById(courseId), "게시글이 존재하지 않습니다.");
    CourseComment courseComment = courseCommentRepository.findById(courseCommentId)
        .orElseThrow(() -> new NotFoundException("삭제 불가: 존재하지 않은 댓글입니다"));
    validateAuth(!courseComment.getUserId().equals(userId), "댓글의 삭제 권한이 없습니다.");

    if (!Objects.isNull(courseComment.getRootCommentId())) {
      deleteParentComment(courseComment);
      courseComment.changeVisibility(FALSE);
      return;
    }

    if (courseCommentRepository.findSubCommentCount(courseComment.getId()).equals(0L)) {
      courseComment.changeVisibility(FALSE);
      return;
    }

    courseComment.changeVisibility(DELETED_INFORMATION);
  }

  private Course getCourse(Long courseId) {
    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new NotFoundException("게시글이 존재하지 않습니다. courseId:" + courseId));
    return course;
  }

  private void validateAuth(boolean courseComment, String message) {
    if (courseComment) {
      throw new UnAuthorizedException(message);
    }
  }

  private void deleteParentComment(CourseComment courseComment) {
    CourseComment parentComment
        = courseCommentRepository.findById(courseComment.getRootCommentId())
        .orElseThrow(() -> new NotFoundException("부모 댓글이 존재하지 않습니다."));

    if (courseCommentRepository.findSubCommentCount(courseComment.getRootCommentId()).equals(1L)
        && parentComment.getVisibility().equals(DELETED_INFORMATION)) {
      parentComment.changeVisibility(FALSE);
    }
  }
}