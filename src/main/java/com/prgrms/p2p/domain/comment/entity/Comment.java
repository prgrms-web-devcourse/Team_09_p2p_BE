package com.prgrms.p2p.domain.comment.entity;

import com.prgrms.p2p.domain.common.BaseEntity;
import com.prgrms.p2p.domain.common.exception.BadRequestException;
import com.prgrms.p2p.domain.common.exception.UnAuthorizedException;
import com.prgrms.p2p.domain.user.entity.User;
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

  public Comment(String comment, Long rootCommentId, Long userId) {
    setComment(comment);
    setRootCommentId(rootCommentId);
    setUserId(userId);
  }

  public void changeComment(String newComment) {
    setComment(newComment);
  }

  public void getAuthForUpdate(User user) {
    if (this.userId.equals(user.getId())) {
      return ;
    }
    throw new UnAuthorizedException("수정 권한이 없습니다.");
  }

  public void getAuthForDelete(User user) {

    if (user.getAuthorities().contains("ROLE_ADMIN")) {
      return ;
    }

    if (this.userId.equals(user.getId())) {
      return ;
    }
    throw new UnAuthorizedException("삭제 권한이 없습니다.");
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

  private void validateNull(Object comment, String message) {
    if (Objects.isNull(comment)) {
      throw new BadRequestException(message);
    }
  }
}
