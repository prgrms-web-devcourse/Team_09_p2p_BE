package com.prgrms.p2p.domain.comment.controller;

import com.prgrms.p2p.domain.comment.dto.CreateCourseCommentRequest;
import com.prgrms.p2p.domain.comment.service.CourseCommentService;
import com.prgrms.p2p.domain.user.aop.annotation.CurrentUser;
import com.prgrms.p2p.domain.user.pojo.CustomUserDetails;
import java.net.URI;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/courses")
public class CourseCommentController {

  private final CourseCommentService courseCommentService;

  @PostMapping("/{course_id}/comments")
  public ResponseEntity<Long> createComment(
      @CurrentUser CustomUserDetails user,
      @RequestBody CreateCourseCommentRequest createCommentReq,
      @PathVariable("course_id") Long courseId) {

    if (Objects.isNull(user)) {
      throw new RuntimeException("로그인이 필요합니다.");
    }
    Long commentId = courseCommentService.save(createCommentReq, courseId, user.getId());
    return ResponseEntity.ok(commentId);
  }

}
