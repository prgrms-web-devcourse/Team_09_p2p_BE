package com.prgrms.p2p.domain.comment.entity;

import com.prgrms.p2p.domain.common.BaseEntity;
import com.prgrms.p2p.domain.common.exception.BadRequestException;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


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
    setComment(comment);
    setRootCommentId(rootCommentId);
    setUserId(userId);
    setSeq(seq);
  }

  public void changeComment(String newComment) {
    setComment(newComment);
  }

  private void setComment(String comment) {
    validateNull(comment, "댓글 내용이 null 일 수 없습니다.");
    this.comment = comment;
  }

  private void setRootCommentId(Long rootCommentId) {
    this.rootCommentId = rootCommentId;
  }

  private void setUserId(Long userId) {
    validateNull(userId,"댓글 작성자(userId)는 null 일 수 없습니다.");
    this.userId = userId;
  }

  private void setSeq(Long seq) {
    validateNull(seq, "댓글 순서(sequence)는 null 일 수 없습니다.");
    this.seq = seq;
  }

  private void validateNull(Object comment, String message) {
    if (Objects.isNull(comment)) {
      throw new BadRequestException(message);
    }
  }
}
