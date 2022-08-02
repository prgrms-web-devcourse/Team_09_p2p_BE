package com.prgrms.p2p.domain.comment.dto;

import java.util.Optional;
import lombok.Getter;

@Getter
public class CreateCourseCommentRequest {

  private String comment;
  private Optional<Long> rootCommentId;
  private Long userId;
  private Long courseId;
}