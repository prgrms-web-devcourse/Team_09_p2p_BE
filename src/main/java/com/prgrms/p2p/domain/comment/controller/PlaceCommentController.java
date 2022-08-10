package com.prgrms.p2p.domain.comment.controller;

import com.prgrms.p2p.domain.comment.dto.CreateCommentRequest;
import com.prgrms.p2p.domain.comment.dto.PlaceCommentResponse;
import com.prgrms.p2p.domain.comment.dto.UpdateCommentRequest;
import com.prgrms.p2p.domain.comment.service.PlaceCommentService;
import com.prgrms.p2p.domain.comment.service.SearchPlaceCommentService;
import com.prgrms.p2p.domain.common.exception.UnAuthorizedException;
import com.prgrms.p2p.domain.user.aop.annotation.CurrentUser;
import com.prgrms.p2p.domain.user.dto.SignUpResponse;
import com.prgrms.p2p.domain.user.pojo.CustomUserDetails;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import java.util.Objects;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"장소 댓글 Controller"})
@RestController
@RequestMapping("/api/v1/places")
public class PlaceCommentController {

  private PlaceCommentService placeCommentService;
  private SearchPlaceCommentService searchPlaceCommentService;

  public PlaceCommentController(
      PlaceCommentService placeCommentService,
      SearchPlaceCommentService searchPlaceCommentService) {
    this.placeCommentService = placeCommentService;
    this.searchPlaceCommentService = searchPlaceCommentService;
  }

  //장소 댓글 등록
  @Operation(summary = "장소 댓글 등록", description = "장소에 대한 댓글을 등록할 수 있습니다.")
  @ApiResponses(value = {
      @ApiResponse(code = 201, message = "장소 댓글 등록 성공", response = Long.class),
      @ApiResponse(code = 401, message = "인증 받지 않은 사용자")
  })
  @PostMapping("/{place_id}/comments")
  public ResponseEntity<Long> createComment(
      @CurrentUser CustomUserDetails user,
      @RequestBody CreateCommentRequest commentRequest,
      @PathVariable("place_id") Long placeId) {

    validateLoginUser(user);
    Long commentId = placeCommentService.save(commentRequest, placeId, user.getId());
    return ResponseEntity.ok(commentId);
  }

  //장소 댓글 수정
  @Operation(summary = "장소 댓글 수정", description = "장소에 대한 댓글을 수정할 수 있습니다.")
  @ApiResponses(value = {
      @ApiResponse(code = 201, message = "장소 댓글 수정 성공", response = Long.class),
      @ApiResponse(code = 401, message = "인증 받지 않은 사용자"),
      @ApiResponse(code = 403, message = "잘못된 값 입력")
  })
  @PutMapping("/{place_id}/comments/{comment_id}")
  public ResponseEntity updateComment(
      @CurrentUser CustomUserDetails user,
      @RequestBody UpdateCommentRequest updateCommentRequest,
      @PathVariable("place_id") Long placeId,
      @PathVariable("comment_id") Long commentId) {

    validateLoginUser(user);

    Long updatedCommentId = placeCommentService.updatePlaceComment(
        updateCommentRequest,
        placeId,
        commentId,
        user.getId());

    return ResponseEntity.ok(updatedCommentId);
  }

  //장소 댓글 삭제
  @Operation(summary = "장소 댓글 삭제", description = "장소에 대한 댓글을 삭제할 수 있습니다.")
  @ApiResponses(value = {
      @ApiResponse(code = 204, message = "장소 댓글 삭제 성공"),
      @ApiResponse(code = 401, message = "인증 받지 않은 사용자"),
      @ApiResponse(code = 403, message = "잘못된 값 입력")
  })
  @DeleteMapping("/{place_id}/comments/{comment_id}")
  public ResponseEntity<Void> deleteComment(
      @CurrentUser CustomUserDetails user,
      @PathVariable("place_id") Long placeId,
      @PathVariable("comment_id") Long commentId) {

    validateLoginUser(user);
    placeCommentService.deletePlaceComment(placeId, commentId, user.getId());
    return ResponseEntity.noContent().build();
  }

  //장소 댓글 전체 조회
  @Operation(summary = "장소에 대한 댓글 조회", description = "장소에 대한 댓글전체를 조회할 수 있습니다.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "장소에 대한 댓글 조회 성공", response = PlaceCommentResponse.class),
      @ApiResponse(code = 204, message = "댓글이 존재하지 않음")
  })
  @GetMapping("/{place_id}/comments")
  public ResponseEntity<PlaceCommentResponse> getCommentList(
      @PathVariable("place_id") Long placeId) {

    PlaceCommentResponse placeComment = searchPlaceCommentService.findPlaceComment(placeId);

    return ResponseEntity.ok(placeComment);
  }

  private void validateLoginUser(CustomUserDetails user) {
    if (Objects.isNull(user)) {
      throw new UnAuthorizedException("로그인이 필요합니다.");
    }
  }
}
