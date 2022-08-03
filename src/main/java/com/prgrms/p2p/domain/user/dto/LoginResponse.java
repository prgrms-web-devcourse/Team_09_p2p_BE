package com.prgrms.p2p.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginResponse {

  private String accessToken;
  private Datas user;

  @Getter
  @NoArgsConstructor
  public class Datas {
    private Long id;
    private String nickname;
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

