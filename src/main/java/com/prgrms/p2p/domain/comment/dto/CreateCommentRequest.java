package com.prgrms.p2p.domain.comment.dto;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentRequest {

  @ApiParam(value = "작성할 댓글 내용", required = true)
  private String comment;

  @ApiParam(value = "대댓글 작성 시 부모 댓글의 ID")
  private Long rootCommentId;
}