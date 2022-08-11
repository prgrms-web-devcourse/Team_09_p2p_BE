package com.prgrms.p2p.domain.course.exception;

import com.prgrms.p2p.domain.common.exception.BadRequestException;

public class CoursePlaceAndImageSizeNotEqualException extends BadRequestException {

  public CoursePlaceAndImageSizeNotEqualException(String message) {
    super(message);
  }
}
