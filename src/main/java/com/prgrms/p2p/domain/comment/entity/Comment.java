package com.prgrms.p2p.domain.comment.entity;

import com.prgrms.p2p.domain.common.BaseEntity;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;


@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE room SET is_deleted = true WHERE id = ?")
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

  // TODO: 2022/08/02 index입니다. 하위링크 참고
  // TODO: 2022/08/02 https://hashcode.co.kr/questions/3480/%EB%8C%80%EB%8C%93%EA%B8%80-db-%EC%8A%A4%ED%82%A4%EB%A7%88-%EC%84%A4%EA%B3%84
  @Column(name = "seq")
  private Long seq;

  @Column(name = "is_deleted")
  private Boolean isDeleted = Boolean.FALSE;

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
