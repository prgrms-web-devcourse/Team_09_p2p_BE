package com.prgrms.p2p.domain.course.exception;

import com.prgrms.p2p.domain.common.exception.BadRequestException;

public class LessThanZeroSeqBadRequestException extends BadRequestException {

  public LessThanZeroSeqBadRequestException(String message) {
    super(message);
  }
}
