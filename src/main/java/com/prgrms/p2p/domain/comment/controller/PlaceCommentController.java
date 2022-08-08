package com.prgrms.p2p.domain.comment.controller;

import com.prgrms.p2p.domain.comment.dto.CreateCommentRequest;
import com.prgrms.p2p.domain.comment.service.PlaceCommentService;
import com.prgrms.p2p.domain.common.exception.UnAuthorizedException;
import com.prgrms.p2p.domain.user.aop.annotation.CurrentUser;
import com.prgrms.p2p.domain.user.pojo.CustomUserDetails;
import java.util.Objects;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/places")
public class PlaceCommentController {

  private PlaceCommentService placeCommentService;

  public PlaceCommentController(
      PlaceCommentService placeCommentService) {
    this.placeCommentService = placeCommentService;
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

  private void validateLoginUser(CustomUserDetails user) {
    if (Objects.isNull(user)) {
      throw new UnAuthorizedException("로그인이 필요합니다.");
    }
  }
}
