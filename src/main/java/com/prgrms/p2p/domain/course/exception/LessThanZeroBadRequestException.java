package com.prgrms.p2p.domain.course.exception;

import com.prgrms.p2p.domain.common.exception.BadRequestException;

public class LessThanZeroBadRequestException extends BadRequestException {

  public LessThanZeroBadRequestException(String message) {
    super(message);
  }
}
