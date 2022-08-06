package com.prgrms.p2p.domain.comment.dto;

import com.prgrms.p2p.domain.comment.entity.Visibility;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CourseCommentForQueryDsl {

  private Long id;
  private String comment;
  private Long rootCommentId;
  private Long courseId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Visibility visibility;

  private Long userId;
  private String userNickName;
  private String userProfileImage;

  public CourseCommentForQueryDsl(Long id, String comment, Long rootCommentId, Long courseId,
      LocalDateTime createdAt, LocalDateTime updatedAt, Visibility visibility, Long userId,
      String userNickName, String userProfileImage) {
    this.id = id;
    this.comment = comment;
    this.rootCommentId = rootCommentId;
    this.courseId = courseId;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.visibility = visibility;
    this.userId = userId;
    this.userNickName = userNickName;
    this.userProfileImage = userProfileImage;
  }

  public void isDeletedComment() {
    this.comment = "삭제된 댓글입니다.";
    this.userNickName = null;
    this.userProfileImage = null;
    this.createdAt = null;
    this.updatedAt = null;
  }
}