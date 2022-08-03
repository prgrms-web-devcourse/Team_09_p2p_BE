package com.prgrms.p2p.domain.comment.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateCourseCommentRequest {

  private String comment;
  private Optional<Long> rootCommentId;
  private Long userId;
  private Long courseId;

  @JsonSetter
  public void setRootCommentId(Long rootCommentId) {
    this.rootCommentId = Optional.ofNullable(rootCommentId);
  }
}