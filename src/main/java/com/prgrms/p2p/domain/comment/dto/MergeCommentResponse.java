package com.prgrms.p2p.domain.comment.dto;

import io.swagger.annotations.ApiParam;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MergeCommentResponse implements Comparable<MergeCommentResponse> {

  @ApiParam(value = "댓글 ID")
  private Long id;

  @ApiParam(value = "부모 댓글 ID")
  private Long rootCommentId;

  @ApiParam(value = "댓글 내용")
  private String comment;

  @ApiParam(value = "댓글 작성 시간")
  private LocalDateTime createdAt;

  @ApiParam(value = "댓글 최근 수정 시간")
  private LocalDateTime updatedAt;

  @ApiParam(value = "댓글에 작성된 대댓글 총 개수")
  private Number subCommentCount;

  @ApiParam(value = "댓글 작성자 ID")
  private Long userId;

  @ApiParam(value = "댓글이 작성된 게시물(코스, 장소) 정보")
  private Content content;

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Content {

    @ApiParam(value = "게시물(코스, 장소) ID")
    private Long id;

    @ApiParam(value = "게시물 타입(코스 or 장소)")
    private String type;

    @ApiParam(value = "게시물 제목(코스 제목 or 장소 이름)")
    private String title;
  }

  @Override
  public int compareTo(MergeCommentResponse o) {
    if (o.createdAt.isBefore(createdAt)) {
      return 1;
    } else if (o.createdAt.isAfter(createdAt)) {
      return -1;
    }
    return 0;
  }
}