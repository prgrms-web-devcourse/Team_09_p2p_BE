package com.prgrms.p2p.domain.user.dto;

import com.prgrms.p2p.domain.user.entity.Sex;
import lombok.Getter;

@Getter
public class SignUpRequest {

  private String email;
  private String password;
  private String passwordCheck;
  private String nickname;
  private String birth;
  private Sex sex;

  public SignUpRequest(String email, String password, String passwordCheck, String nickname,
      String birth, Sex sex) {
    this.email = email;
    this.password = password;
    this.passwordCheck = passwordCheck;
    this.nickname = nickname;
    this.birth = birth;
    this.sex = sex;
  }
}
