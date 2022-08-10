package com.prgrms.p2p.domain.comment.dto;

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

  private Long id;
  private Long rootCommentId;
  private String comment;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Number subCommentCount;
  private Long userId;
  private Content content;

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Content {

    private Long id;
    private String type;
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