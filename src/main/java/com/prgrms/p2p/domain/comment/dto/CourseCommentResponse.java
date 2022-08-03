package com.prgrms.p2p.domain.comment.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CourseCommentResponse {

  private Long id;
  private String comment;
  private Long rootCommentId;
  private Long courseId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private Long userId;
  private String userNickName;
  private String userProfileImage;

  public CourseCommentResponse(Long id, String comment, Long rootCommentId, Long courseId,
      LocalDateTime createdAt, LocalDateTime updatedAt, Long userId, String userNickName,
      String userProfileImage) {
    this.id = id;
    this.comment = comment;
    this.rootCommentId = rootCommentId;
    this.courseId = courseId;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.userId = userId;
    this.userNickName = userNickName;
    this.userProfileImage = userProfileImage;
  }
}
