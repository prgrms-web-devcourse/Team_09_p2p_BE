package com.prgrms.p2p.domain.user.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "로그인 응답 DTO")
@Getter
@Setter
@Builder
public class LoginResponse {

  @Schema(description = "토큰 값", example = "tokentokentoken")
  private String accessToken;

  private Datas user;

  @Schema(description = "로그인 응답 내 유저 정보")
  @Getter
  @NoArgsConstructor
  public class Datas {
    @Schema(description = "로그인한 유저의 아이디", example = "1")
    private Long id;

    @Schema(description = "로그인한 유저의 닉네임", example = "beomsic")
    private String nickname;

    @Schema(description = "로그인한 유저의 프로필 이미지", example = "http://image.com")
    private String profileImage;

    public Datas(Long id, String nickname, String profileImage) {
      this.id = id;
      this.nickname = nickname;
      this.profileImage = profileImage;
    }
  }

  public LoginResponse(String accessToken, Datas user) {
    this.accessToken = accessToken;
    this.user = user;
  }
}

