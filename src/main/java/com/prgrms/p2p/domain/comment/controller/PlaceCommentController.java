package com.prgrms.p2p.domain.comment.controller;

import com.prgrms.p2p.domain.comment.dto.CreateCommentRequest;
import com.prgrms.p2p.domain.comment.dto.PlaceCommentResponse;
import com.prgrms.p2p.domain.comment.dto.UpdateCommentRequest;
import com.prgrms.p2p.domain.comment.service.PlaceCommentService;
import com.prgrms.p2p.domain.comment.service.SearchPlaceCommentService;
import com.prgrms.p2p.domain.common.exception.UnAuthorizedException;
import com.prgrms.p2p.domain.user.aop.annotation.CurrentUser;
import com.prgrms.p2p.domain.user.pojo.CustomUserDetails;
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
  @DeleteMapping("/{place_id}/comments/{comment_id}")
  public ResponseEntity<Void> deleteComment(
      @CurrentUser CustomUserDetails user,
      @PathVariable("place_id") Long placeId,
      @PathVariable("comment_id") Long commentId) {

    validateLoginUser(user);
    placeCommentService.deletePlaceComment(placeId,commentId,user.getId());
    return ResponseEntity.noContent().build();
  }

  private void validateLoginUser(CustomUserDetails user) {
    if (Objects.isNull(user)) {
      throw new UnAuthorizedException("로그인이 필요합니다.");
    }
  }

  //장소 댓글 전체 조회
  @GetMapping("/{place_id}/comments")
  public ResponseEntity<PlaceCommentResponse> getCommentList(@PathVariable("place_id") Long placeId) {

    PlaceCommentResponse placeComment = searchPlaceCommentService.findPlaceComment(placeId);

    return ResponseEntity.ok(placeComment);
  }

}
