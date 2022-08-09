package com.prgrms.p2p.domain.comment.dto;

import com.prgrms.p2p.domain.comment.entity.Visibility;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlaceCommentForQueryDsl {
  private Long id;
  private String comment;
  private Long rootCommentId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Visibility visibility;

  private Long userId;
  private String userNickName;
  private String userProfileImage;

  public void isDeletedComment() {
    this.comment = "삭제된 댓글입니다.";
    this.userNickName = null;
    this.userProfileImage = null;
    this.createdAt = null;
    this.updatedAt = null;
  }
}
