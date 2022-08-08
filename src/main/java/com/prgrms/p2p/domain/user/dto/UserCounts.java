package com.prgrms.p2p.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "유저의 좋아요, 북마크, 게시물, 댓글 수")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCounts {

  @Schema(description = "유저가 등록한 코스의 개수", example = "1")
  private Long course;

  @Schema(description = "유저가 남긴 댓글 수", example = "1")
  private Long comments;

  @Schema(description = "유저가 즐겨찾기한 수")
  private UserBookmarkResponse bookmarks;

}
