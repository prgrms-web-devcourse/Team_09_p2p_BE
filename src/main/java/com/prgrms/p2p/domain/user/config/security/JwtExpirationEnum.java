package com.prgrms.p2p.domain.user.config.security;

public enum JwtExpirationEnum {
  ACCESS_TOKEN_EXPIRATION_TIME("AccessToken 만료 시간 / 1시간", 1000L * 60 * 60),
  REFRESH_TOKEN_EXPIRATION_TIME("Refresh 토큰 만료 시간 / 7일", 1000L * 60 * 60 * 24 * 7),
  REISSUE_EXPIRATION_TIME("Refresh 토큰 만료 시간 / 3일", 1000L * 60 * 60 * 24 * 3);

  private String description;
  private Long value;

  JwtExpirationEnum(String description, Long value) {
    this.description = description;
    this.value = value;
  }

  public String getDescription() {
    return description;
  }

  public Long getValue() {
    return value;
  }
}

