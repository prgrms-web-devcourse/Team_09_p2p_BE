package com.prgrms.p2p.domain.comment.controller;

import com.prgrms.p2p.domain.comment.dto.MergeCommentResponse;
import com.prgrms.p2p.domain.comment.service.MergeCommentService;
import com.prgrms.p2p.domain.common.exception.BadRequestException;
import com.prgrms.p2p.domain.user.aop.annotation.CurrentUser;
import com.prgrms.p2p.domain.user.pojo.CustomUserDetails;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
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
      @PageableDefault(page = 0, size = 15) Pageable pageable) {

    if (Objects.isNull(user) && userId.isEmpty()) {
      throw new BadRequestException("잘못된 요청(로그인 or 조회하고싶은 유저의 아이디가 필요)");
    }
    Long targetUserId = userId.isEmpty() ? user.getId() : userId.get();
    Slice<MergeCommentResponse> commentsByUserId
        = mergeCommentService.findCommentsByUserId(targetUserId, pageable);

    return ResponseEntity.ok(commentsByUserId);
  }
}
