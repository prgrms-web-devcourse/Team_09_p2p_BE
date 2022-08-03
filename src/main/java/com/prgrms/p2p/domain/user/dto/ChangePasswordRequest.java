package com.prgrms.p2p.domain.user.dto;

import lombok.Getter;


@Getter
public class ChangePasswordRequest {

  private String oldPassword;

  private String newPassword;

}
