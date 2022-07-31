package com.prgrms.p2p.domain.like.entity;

import com.prgrms.p2p.domain.common.BaseEntity;
import javax.persistence.Column;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Like extends BaseEntity {

  @Column(name = "user_id")
  private Long userId;

  public Like(Long userId) {
    setUserId(userId);
  }

  private void setUserId(Long userId) {
    if (ObjectUtils.isEmpty(userId) || userId <= 0) {
      throw new IllegalArgumentException("잘못된 사용자 아이디가 입력되었습니다.");
    }
    this.userId = userId;
  }
}
