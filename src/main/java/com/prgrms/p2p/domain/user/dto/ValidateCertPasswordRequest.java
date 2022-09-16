package com.prgrms.p2p.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ValidateCertPasswordRequest {

  private String email;

  private String certNumber;

}
