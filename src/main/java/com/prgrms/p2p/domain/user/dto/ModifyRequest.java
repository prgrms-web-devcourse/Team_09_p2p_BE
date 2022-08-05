package com.prgrms.p2p.domain.user.dto;

import static java.util.Objects.isNull;
import static org.apache.logging.log4j.util.Strings.isBlank;

import com.prgrms.p2p.domain.user.entity.Sex;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "로그인 응답 DTO")
@Getter
@NoArgsConstructor
public class ModifyRequest {

  @Schema(description = "닉네임", example = "beomsic1")
  private String nickname;

  @Schema(description = "생년월일", example = "1997-11-29")
  private String birth;

  @Schema(description = "성별", example = "female")
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
