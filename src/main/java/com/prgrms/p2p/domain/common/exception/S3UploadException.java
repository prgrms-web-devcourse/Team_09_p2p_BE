package com.prgrms.p2p.domain.common.exception;

public class S3UploadException extends RuntimeException {

  private static final String S3_EXCEPTION = "Amazon S3에 업로드하는 과정에서 문제가 발생했습니다.";

  public S3UploadException() {
    super(S3_EXCEPTION);
  }

  public S3UploadException(String message) {
    super(message);
  }
}
