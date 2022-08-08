package com.prgrms.p2p.domain.comment.controller;

import com.prgrms.p2p.domain.comment.dto.CourseCommentResponse;
import com.prgrms.p2p.domain.comment.dto.CreateCommentRequest;
import com.prgrms.p2p.domain.comment.dto.UpdateCommentRequest;
import com.prgrms.p2p.domain.comment.service.CourseCommentService;
import com.prgrms.p2p.domain.comment.service.SearchCourseCommentService;
import com.prgrms.p2p.domain.common.exception.UnAuthorizedException;
import com.prgrms.p2p.domain.user.aop.annotation.CurrentUser;
import com.prgrms.p2p.domain.user.pojo.CustomUserDetails;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/courses")
public class CourseCommentController {

  private final CourseCommentService courseCommentService;
  private final SearchCourseCommentService searchCourseCommentService;

  @PostMapping("/{course_id}/comments")
  public ResponseEntity<Long> createComment(
      @CurrentUser CustomUserDetails user,
      @RequestBody CreateCommentRequest createCommentReq,
      @PathVariable("course_id") Long courseId) {

    validateLoginUser(user);
    Long commentId = courseCommentService.save(createCommentReq, courseId, user.getId());
    return ResponseEntity.ok(commentId);
  }

  @GetMapping("/{course_id}/comments")
  public ResponseEntity<CourseCommentResponse> getCourseCommentList(
      @PathVariable("course_id") Long courseId
  ) {
    CourseCommentResponse courseComment = searchCourseCommentService.findCourseComment(courseId);
    return ResponseEntity.ok(courseComment);
  }

  @PutMapping("/{course_id}/comments/{comment_id}")
  public ResponseEntity<Long> updateCourseComment(
      @CurrentUser CustomUserDetails user,
      @PathVariable("course_id") Long courseId,
      @PathVariable("comment_id") Long commentId,
      @RequestBody UpdateCommentRequest updateCommentRequest
  ) {
    validateLoginUser(user);
    Long updatedCommentId =
        courseCommentService.updateCourseComment(
            updateCommentRequest,
            commentId,
            courseId,
            user.getId()
        );

    return ResponseEntity.ok(updatedCommentId);
  }

  @DeleteMapping("/{course_id}/comments/{comment_id}")
  public ResponseEntity<?> deleteCourseComment(
      @CurrentUser CustomUserDetails user,
      @PathVariable("course_id") Long courseId,
      @PathVariable("comment_id") Long commentId
  ) {
    validateLoginUser(user);
    courseCommentService.deleteCourseComment(commentId, courseId, user.getId());

    return ResponseEntity.ok(null);
  }

  private void validateLoginUser(CustomUserDetails user) {
    if (Objects.isNull(user)) {
      throw new UnAuthorizedException("로그인이 필요합니다.");
    }
  }
}
