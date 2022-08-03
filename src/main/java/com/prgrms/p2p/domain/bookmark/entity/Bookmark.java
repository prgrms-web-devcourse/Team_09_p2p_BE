package com.prgrms.p2p.domain.bookmark.entity;

import com.prgrms.p2p.domain.common.BaseEntity;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Bookmark extends BaseEntity {

  @Column(name = "user_id")
  private Long userId;

  public Bookmark(Long userId) {
    setUserId(userId);
  }

  private void setUserId(Long userId) {
    if (Objects.isNull(userId) || userId <= 0) {
      throw new IllegalArgumentException("잘못된 사용자 아이디가 입력되었습니다.");
    }
    this.userId = userId;
  }
}
