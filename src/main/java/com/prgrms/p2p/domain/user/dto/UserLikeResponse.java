package com.prgrms.p2p.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "유저가 누른 좋아요")
@Getter
@Builder
@NoArgsConstructor
public class UserLikeResponse {
  @Schema(description = "유저가 좋아요 누른 총 개수", example = "4")
  private Long total;

  @Schema(description = "유저가 코스에 좋아요 누른 개수", example = "2")
  private Long courseLike;

  @Schema(description = "유저가 장소에 좋아요 누른 개수", example = "2")
  private Long placeLike;

  public UserLikeResponse(Long total, Long courseLike, Long placeLike) {
    this.total = total;
    this.courseLike = courseLike;
    this.placeLike = placeLike;
  }
}
