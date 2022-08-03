package com.prgrms.p2p.domain.user.dto;

import static java.util.Objects.isNull;
import static org.apache.logging.log4j.util.Strings.isBlank;

import com.prgrms.p2p.domain.user.entity.Sex;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ModifyRequest {
  private String nickname;
  private String birth;
  private Sex sex;

  public ModifyRequest(String nickname, String birth, Sex sex) {
    this.nickname = nickname;
    this.birth = birth;
    this.sex = sex;
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
