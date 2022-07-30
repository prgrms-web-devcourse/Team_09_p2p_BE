package com.prgrms.p2p.domain.user.config.security;

public enum JwtHeader {

  GRANT_TYPE("Jwt Header", "Bearer ");

  private String description;
  private String value;

  JwtHeader(String description, String value) {
    this.description = description;
    this.value = value;
  }

  public String getValue() {
    return value;
  }

}
