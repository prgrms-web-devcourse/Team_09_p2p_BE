package com.prgrms.p2p.domain.user.util;

public class Validation {

  public static void validatePassword(String password) {
    String pattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{12,}$";

    // TODO: exception 다시 만들기 - InvalidPasswordException
    if(!pattern.matches(password)) {
      throw new IllegalArgumentException();
    }
  }

  public static void validateEmail(String email) {
    String pattern = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";

    // TODO: exception 다시 만들기 - InvalidEmailException
    if(!pattern.matches(email)) {
      throw new IllegalArgumentException();
    }
  }

}
