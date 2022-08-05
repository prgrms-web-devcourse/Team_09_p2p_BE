package com.prgrms.p2p.domain.comment.entity;

import com.prgrms.p2p.domain.common.BaseEntity;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;


@Getter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Comment extends BaseEntity {

  @Column(name = "comment")
  private String comment;

  @Column(name = "root_comment_id")
  private Long rootCommentId;

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "seq")
  private Long seq;

  public Comment(String comment, Long rootCommentId, Long userId, Long seq) {
    this.comment = comment;
    this.rootCommentId = rootCommentId;
    this.userId = userId;
    this.seq = seq;
  }

  public void changeComment(String newComment) {
    this.comment = newComment;
  }

  private void setComment(String comment) {
    this.comment = comment;
  }

  private void setUserId(Long userId) {
    this.userId = userId;
  }

  private Comment(Long seq) {
    this.seq = seq;
  }
}
