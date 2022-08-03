package com.prgrms.p2p.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class ChangePasswordRequest {

  private String oldPassword;
  private String newPassword;

}
