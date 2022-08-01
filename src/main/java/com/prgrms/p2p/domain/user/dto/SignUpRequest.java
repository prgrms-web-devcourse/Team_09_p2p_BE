package com.prgrms.p2p.domain.user.dto;

import static java.util.Objects.isNull;
import static org.apache.logging.log4j.util.Strings.isBlank;

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
    setEmail(email);
    setPassword(password);
    setPasswordCheck(passwordCheck);
    setNickname(nickname);
    setBirth(birth);
    setSex(sex);
  }

  public void setEmail(String email) {
    checkBlank(email);
    this.email = email;
  }

  public void setPassword(String password) {
    checkBlank(password);
    this.password = password;
  }

  public void setPasswordCheck(String passwordCheck) {
    checkBlank(passwordCheck);
    this.passwordCheck = passwordCheck;
  }

  public void setNickname(String nickname) {
    checkBlank(nickname);
    this.nickname = nickname;
  }

  public void setBirth(String birth) {
    checkBlank(birth);
    this.birth = birth;
  }

  public void setSex(Sex sex) {
    if(isNull(sex)) throw new RuntimeException();
    this.sex = sex;
  }

  private void checkBlank(String target) {
    //TODO: InvalidParamException
    if(isBlank(target)) {
      throw new RuntimeException();
    }
  }

}
