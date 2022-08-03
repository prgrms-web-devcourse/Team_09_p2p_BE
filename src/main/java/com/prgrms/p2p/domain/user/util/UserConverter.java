package com.prgrms.p2p.domain.user.util;

import com.prgrms.p2p.domain.user.dto.LoginResponse;
import com.prgrms.p2p.domain.user.dto.OtherUserDetailResponse;
import com.prgrms.p2p.domain.user.dto.SignUpRequest;
import com.prgrms.p2p.domain.user.dto.UserDetailResponse;
import com.prgrms.p2p.domain.user.entity.Authority;
import com.prgrms.p2p.domain.user.entity.User;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserConverter {

  public static User toUser(SignUpRequest signUpRequest) {
    User user = new User(
        signUpRequest.getEmail(),
        PasswordEncrypter.encrypt(signUpRequest.getPassword()),
        signUpRequest.getNickname(),
        signUpRequest.getBirth(),
        signUpRequest.getSex()
    );
    user.addAuthority(Authority.ofUser(user));
    return user;
  }

  public static User toAdmin(SignUpRequest signUpRequest) {
    User user = new User(
        signUpRequest.getEmail(),
        PasswordEncrypter.encrypt(signUpRequest.getPassword()),
        signUpRequest.getNickname(),
        signUpRequest.getBirth(),
        signUpRequest.getSex()
    );
    user.addAuthority(Authority.ofAdmin(user));
    return user;
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


  public static OtherUserDetailResponse otherDetailFromUser(User user) {

    String profileUrl = user.getProfileUrl()
        .orElse(null);

    return OtherUserDetailResponse.builder()
        .id(user.getId())
        .nickname(user.getNickname())
        .profileImage(profileUrl)
        .birth(fromLocalDate(user.getBirth()))
        .sex(user.getSex())
        .createdAt(fromLocalDateTime(user.getCreatedAt()))
        .build();
  }

  private static String fromLocalDate(LocalDate date) {
    return String.valueOf(date);
  }

  private static String fromLocalDateTime(LocalDateTime date) {
    return String.valueOf(date);
  }

  public static LoginResponse fromUserAndToken(User user, String token) {
    String profileUrl = user.getProfileUrl()
        .orElse(null);

    LoginResponse response = LoginResponse.builder()
        .accessToken(token)
        .build();

    LoginResponse.Datas data = response.new Datas(
        user.getId(),
        user.getNickname(),
        profileUrl
    );
    response.setUser(data);
    return response;
  }
}
