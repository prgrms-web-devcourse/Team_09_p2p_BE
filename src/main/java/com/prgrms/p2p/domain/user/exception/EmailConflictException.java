package com.prgrms.p2p.domain.user.exception;

import com.prgrms.p2p.domain.common.exception.ConflictException;

public class EmailConflictException extends ConflictException {

  private static final String EMAIL_CONFLICT = "이미 등록되어 있는 이메일 입니다.";

  public EmailConflictException() {
    super(EMAIL_CONFLICT);
  }

  public EmailConflictException(String message) {
    super(message);
  }
}
