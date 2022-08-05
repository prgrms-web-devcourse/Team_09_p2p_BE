package com.prgrms.p2p.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "회원가입 응답 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpResponse {

  @Schema(description = "닉네임", example = "beomsic")
  private String nickname;

}
