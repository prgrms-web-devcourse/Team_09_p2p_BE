package com.prgrms.p2p.domain.user.pojo;

import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash("refreshToken")
@Getter
@Builder
public class RefreshToken {

  @Id
  private String email;

  private String refreshToken;

  @TimeToLive
  private Long expiration;

  public static RefreshToken createRefreshToken(String email, String refreshToken, Long remainingMilliSeconds) {
    return RefreshToken.builder()
        .email(email)
        .refreshToken(refreshToken)
        .expiration(remainingMilliSeconds)
        .build();
  }

  //TODO: exception 추가 NotMatchTokenException
  public void checkToken(String token) {
    if(!token.equals(this.refreshToken)) {
      throw new IllegalArgumentException();
    }
  }

}
