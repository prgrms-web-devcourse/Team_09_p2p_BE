package com.prgrms.p2p.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "유저가 누른 북마크")
@Getter
@Builder
@NoArgsConstructor
public class UserBookmarkResponse {

  @Schema(description = "유저가 북마크한 총 개수", example = "4")
  private Long total;

  @Schema(description = "유저가 코스에 북마크한 개수", example = "2")
  private Long courseBookmark;

  @Schema(description = "유저가 장소에 북마크한 개수", example = "2")
  private Long placeBookmark;

  public UserBookmarkResponse(Long total, Long courseBookmark, Long placeBookmark) {
    this.total = total;
    this.courseBookmark = courseBookmark;
    this.placeBookmark = placeBookmark;
  }
}
