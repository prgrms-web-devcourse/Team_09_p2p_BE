package com.prgrms.p2p.domain.bookmark.entity;

import com.prgrms.p2p.domain.common.BaseEntity;
import javax.persistence.Column;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;


@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE room SET is_deleted = true WHERE id = ?")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Bookmark extends BaseEntity {

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "is_deleted")
  private Boolean isDeleted = Boolean.FALSE;

  public Bookmark(String comment, Long userId) {
    this.userId = userId;
  }

  private void setUserId(Long userId) {
    this.userId = userId;
  }
}
