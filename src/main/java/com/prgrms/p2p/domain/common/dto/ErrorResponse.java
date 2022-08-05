package com.prgrms.p2p.domain.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ErrorResponse {

  private String message;

  public ErrorResponse(String message) {
    this.message = message;
  }
}
