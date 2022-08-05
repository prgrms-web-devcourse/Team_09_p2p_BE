package com.prgrms.p2p.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "패스워드 교체 요청 DTO")
@Getter
@NoArgsConstructor
public class ChangePasswordRequest {

  @Schema(description = "현재 비밀번호", example = "test123!!")
  private String oldPassword;

  @Schema(description = "새 비밀번호", example = "test1234!!")
  private String newPassword;

}
