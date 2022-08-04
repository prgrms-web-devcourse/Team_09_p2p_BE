package com.prgrms.p2p.domain.like.entity;

import com.prgrms.p2p.domain.common.BaseEntity;
import com.prgrms.p2p.domain.common.exception.BadRequestException;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

@Getter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Like extends BaseEntity {

  @Column(name = "user_id")
  private Long userId;

  public Like(Long userId) {
    setUserId(userId);
  }

  private void setUserId(Long userId) {
    if (ObjectUtils.isEmpty(userId) || userId <= 0) {
      throw new BadRequestException("잘못된 사용자 아이디가 입력되었습니다.");
    }
    this.userId = userId;
  }
}
