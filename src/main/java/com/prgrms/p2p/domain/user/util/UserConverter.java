package com.prgrms.p2p.domain.user.util;

import com.prgrms.p2p.domain.user.dto.SignUpRequest;
import com.prgrms.p2p.domain.user.entity.User;

public class UserConverter {

  public static User toUser(SignUpRequest signUpRequest) {
    return new User(
            signUpRequest.getEmail(),
            PasswordEncrypter.encrypt(signUpRequest.getPassword()),
            signUpRequest.getNickname(),
            signUpRequest.getBirth(),
            signUpRequest.getSex()
        );
  }
}
