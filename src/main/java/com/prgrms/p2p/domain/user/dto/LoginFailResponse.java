package com.prgrms.p2p.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "로그인 실패 응답 DTO")
@Getter
@NoArgsConstructor
public class LoginFailResponse {

  @Schema(description = "실패 횟수", example = "4")
  private Integer count;

  @Schema(description = "제한 시간", example = "2022-08-04T09:51:24")
  private String expiredAt;

  public LoginFailResponse(Integer count, String expiredAt) {
    this.count = count;
    this.expiredAt = expiredAt;
  }
}
