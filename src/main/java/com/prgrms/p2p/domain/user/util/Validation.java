package com.prgrms.p2p.domain.user.util;

public class Validation {

  public static boolean validatePassword(String password) {
    String pattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d~!@#$%^&*()+|=]{8,15}$";

    // TODO: exception 다시 만들기 - InvalidPasswordException
    if(!password.matches(pattern)) {
      throw new IllegalArgumentException();
    }
    return true;
  }

  public static boolean validateEmail(String email) {
    String pattern = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";

    // TODO: exception 다시 만들기 - InvalidEmailException
    if(!email.matches(pattern)) {
      throw new IllegalArgumentException();
    }
    return true;
  }

  public static boolean validateNickname(String nickname) {
    String pattern = "^.{3,6}$";

    // TODO: exception 다시 만들기 - InvalidNicknameException
    if(!nickname.matches(pattern)) {
      throw new IllegalArgumentException();
    }
    return true;
  }
}
