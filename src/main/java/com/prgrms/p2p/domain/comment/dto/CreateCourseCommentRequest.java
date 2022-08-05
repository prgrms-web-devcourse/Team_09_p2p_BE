package com.prgrms.p2p.domain.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCourseCommentRequest {

  private String comment;
  private Long rootCommentId;
  private Long userId;
  private Long courseId;
}