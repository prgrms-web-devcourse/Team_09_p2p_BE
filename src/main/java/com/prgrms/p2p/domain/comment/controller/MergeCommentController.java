package com.prgrms.p2p.domain.comment.controller;

import com.prgrms.p2p.domain.comment.dto.MergeCommentResponse;
import com.prgrms.p2p.domain.comment.service.MergeCommentService;
import com.prgrms.p2p.domain.user.aop.annotation.CurrentUser;
import com.prgrms.p2p.domain.user.pojo.CustomUserDetails;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class MergeCommentController {

  private final MergeCommentService mergeCommentService;

  @GetMapping
  public ResponseEntity<Slice<MergeCommentResponse>> getMergeCommentByUserId(
      @RequestParam("userId") Optional<Long> userId,
      @CurrentUser CustomUserDetails user,
      Pageable pageable) {

    Slice<MergeCommentResponse> commentsByUserId
        = mergeCommentService.findCommentsByUserId(userId.orElseGet(user::getId), pageable);

    return ResponseEntity.ok(commentsByUserId);
  }
}
