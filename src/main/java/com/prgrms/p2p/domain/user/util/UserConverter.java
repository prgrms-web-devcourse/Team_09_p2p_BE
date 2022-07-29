package com.prgrms.p2p.domain.user.util;

import com.prgrms.p2p.domain.user.dto.SignUpRequest;
import com.prgrms.p2p.domain.user.dto.UserDetailResponse;
import com.prgrms.p2p.domain.user.entity.User;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

  public static UserDetailResponse detailFromUser(User user) {

    String profileUrl = user.getProfileUrl()
        .orElse(null);

    return UserDetailResponse.builder()
        .id(user.getId())
        .email(user.getEmail())
        .nickname(user.getNickname())
        .profileImage(profileUrl)
        .birth(fromLocalDate(user.getBirth()))
        .sex(user.getSex())
        .createdAt(fromLocalDateTime(user.getCreatedAt()))
        .updatedAt(fromLocalDateTime(user.getUpdatedAt()))
        .build();
  }

  private static String fromLocalDate(LocalDate date) {
    return date.toString();
  }

  private static String fromLocalDateTime(LocalDateTime date) {
    return date.toString();
  }

}
