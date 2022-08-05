package com.prgrms.p2p.domain.user.exception;

import lombok.Getter;

@Getter
public class LoginFailException extends RuntimeException {

  private static final String LOGIN_FAIL = "로그인에 실패했습니다.";

  private int count;
  private String expired;

  public LoginFailException(int count, String expired) {
    super(LOGIN_FAIL);
    this.count = count;
    this.expired = expired;
  }
}
