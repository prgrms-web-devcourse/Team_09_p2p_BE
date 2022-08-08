package com.prgrms.p2p.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "유저가 남긴 댓글")
@Getter
@Builder
@NoArgsConstructor
public class UserCommentResponse {

  @Schema(description = "유저가 남긴 총 댓글 수", example = "4")
  private Long total;

  @Schema(description = "유저가 코스에 남긴 댓글 수", example = "2")
  private Long courseComment;

  @Schema(description = "유저가 장소에 남긴 댓글 수", example = "2")
  private Long placeComment;

  public UserCommentResponse(Long total, Long courseComment, Long placeComment) {
    this.total = total;
    this.courseComment = courseComment;
    this.placeComment = placeComment;
  }
}
