package com.prgrms.p2p.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {

  private String accessToken;

  public class User {
    private Long id;
    private String nickname;
    private String profileImage;

    public User(Long id, String nickname, String profileImage) {
      this.id = id;
      this.nickname = nickname;
      this.profileImage = profileImage;
    }
  }

  public LoginResponse(String accessToken) {
    this.accessToken = accessToken;
  }

}

