package com.prgrms.p2p.domain.comment.controller;

import com.prgrms.p2p.domain.comment.dto.CourseCommentResponse;
import com.prgrms.p2p.domain.comment.dto.CreateCommentRequest;
import com.prgrms.p2p.domain.comment.dto.UpdateCommentRequest;
import com.prgrms.p2p.domain.comment.service.CourseCommentService;
import com.prgrms.p2p.domain.comment.service.SearchCourseCommentService;
import com.prgrms.p2p.domain.common.exception.UnAuthorizedException;
import com.prgrms.p2p.domain.user.aop.annotation.Auth;
import com.prgrms.p2p.domain.user.aop.annotation.CurrentUser;
import com.prgrms.p2p.domain.user.pojo.CustomUserDetails;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
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

  @Operation(summary = "댓글 작성 기능", description = "인증된 사용자는 코스에 댓글을 작성할 수 있습니다.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "댓글 작성 성공"),
      @ApiResponse(code = 401, message = "인증 받지 않은 사용자"),
      @ApiResponse(code = 404, message = "존재하지 않은 코스에 댓글 작성 요청")
  })
  @Auth
  @PostMapping("/{course_id}/comments")
  public ResponseEntity<Long> createComment(
      @CurrentUser CustomUserDetails user,
      @RequestBody CreateCommentRequest createCommentReq,
      @PathVariable("course_id") Long courseId) {

    validateLoginUser(user);
    Long commentId = courseCommentService.save(createCommentReq, courseId, user.getId());
    return ResponseEntity.ok(commentId);
  }

  @Operation(summary = "댓글 조회 기능", description = "누구나 코스에 댓글을 조회할 수 있습니다.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "댓글 조회 성공"),
      @ApiResponse(code = 404, message = "존재하지 않은 코스의 댓글 조회 요청")
  })
  @GetMapping("/{course_id}/comments")
  public ResponseEntity<CourseCommentResponse> getCourseCommentList(
      @PathVariable("course_id") Long courseId
  ) {
    CourseCommentResponse courseComment = searchCourseCommentService.findCourseComment(courseId);
    return ResponseEntity.ok(courseComment);
  }

  @Operation(summary = "댓글 수정 기능", description = "인증된 사용자는 코스에 본인이 작성한 댓글을 수정할 수 있습니다.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "댓글 수정 성공"),
      @ApiResponse(code = 401, message = "인증 받지 않은 사용자"),
      @ApiResponse(code = 404, message = "존재하지 않은 코스, 댓글 아이디")
  })
  @Auth
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

  @Operation(summary = "댓글 삭제 기능", description = "인증된 사용자는 코스에 본인이 작성한 댓글을 삭제할 수 있습니다.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "댓글 수정 성공"),
      @ApiResponse(code = 400, message = "삭제된 댓글 재시도"),
      @ApiResponse(code = 401, message = "인증 받지 않은 사용자"),
      @ApiResponse(code = 404, message = "존재하지 않은 코스, 댓글 아이디")
  })
  @Auth
  @DeleteMapping("/{course_id}/comments/{comment_id}")
  public ResponseEntity<Void> deleteCourseComment(
      @CurrentUser CustomUserDetails user,
      @PathVariable("course_id") Long courseId,
      @PathVariable("comment_id") Long commentId
  ) {
    validateLoginUser(user);
    courseCommentService.deleteCourseComment(commentId, courseId, user.getId());

    return ResponseEntity.noContent().build();
  }

  private void validateLoginUser(CustomUserDetails user) {
    if (Objects.isNull(user)) {
      throw new UnAuthorizedException("로그인이 필요합니다.");
    }
  }
}
