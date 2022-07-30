package com.prgrms.p2p.domain.user.dto;

import com.prgrms.p2p.domain.user.entity.Sex;
import lombok.Getter;

@Getter
public class SignUpRequest {

  private String email;
  private String password;
  private String nickname;
  private String birth;
  private Sex sex;

}
