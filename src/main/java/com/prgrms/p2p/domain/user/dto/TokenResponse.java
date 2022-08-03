package com.prgrms.p2p.domain.user.dto;

import com.prgrms.p2p.domain.user.config.security.JwtHeader;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TokenResponse {

  private String grantType;
  private String accessToken;
  private String refreshToken;

  public static TokenResponse of(String accessToken, String refreshToken) {
    return TokenResponse.builder()
        .grantType(JwtHeader.GRANT_TYPE.getValue())
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }

}
