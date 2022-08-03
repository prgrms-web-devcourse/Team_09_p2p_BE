package com.prgrms.p2p.domain.common.exception;

import com.prgrms.p2p.domain.common.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class ExceptionAdvice {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<Void> handleNotFoundException(Exception e) {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(UnAuthorizedException.class)
  public ResponseEntity<ErrorResponse> handleUnAuthorizedException(UnAuthorizedException e) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(e.getMessage()));
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorResponse> badRequestException(BadRequestException e) {
    return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
    return ResponseEntity.internalServerError().body(new ErrorResponse(e.getMessage()));
  }
}
