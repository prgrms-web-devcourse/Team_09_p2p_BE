package com.prgrms.p2p.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "로그인 요청 DTO")
@Getter
@NoArgsConstructor
public class LoginRequest {

  @Schema(description = "이메일", example = "test@gmail.com")
  private String email;

  @Schema(description = "비밀번호", example = "test123!!")
  private String password;

}
