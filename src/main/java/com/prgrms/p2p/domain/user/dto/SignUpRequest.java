package com.prgrms.p2p.domain.user.dto;

import static java.util.Objects.isNull;
import static org.apache.logging.log4j.util.Strings.isBlank;

import com.prgrms.p2p.domain.user.entity.Sex;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "회원가입 요청 DTO")
@Getter
@NoArgsConstructor
public class SignUpRequest {

  @Schema(description = "이메일", example = "test@gmail.com")
  private String email;

  @Schema(description = "비밀번호", example = "test123!!")
  private String password;

  @Schema(description = "비밀번호 확인", example = "test123!!")
  private String passwordCheck;

  @Schema(description = "닉네임", example = "beomsic")
  private String nickname;

  @Schema(description = "생년월일", example = "1997-11-29")
  private String birth;

  @Schema(description = "성별", example = "male")
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
