package com.prgrms.p2p.domain.user.exception;

import com.prgrms.p2p.domain.common.exception.BadRequestException;

public class InvalidPatternException extends BadRequestException {

  private static final String INVALID_PATTERN = "입력한 값이 잘못된 형식입니다.";

  public InvalidPatternException() {
    super(INVALID_PATTERN);
  }

  public InvalidPatternException(String message) {
    super(message);
  }
}
