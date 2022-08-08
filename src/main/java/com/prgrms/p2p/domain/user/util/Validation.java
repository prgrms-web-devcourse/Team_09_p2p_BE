package com.prgrms.p2p.domain.user.util;

import com.prgrms.p2p.domain.user.exception.InvalidPatternException;

public class Validation {

  private static final String EMAIL_PATTERN = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
  private static final String PASSWORD_PATTERN = "^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\\-_=+]).{8,15}$";
  private static final String NICKNAME_PATTERN = "^.{2,8}$";

  public static boolean validatePassword(String password) {
    if (!password.matches(PASSWORD_PATTERN)) {
      throw new InvalidPatternException();
    }
    return true;
  }

  public static boolean validateEmail(String email) {
    // TODO: exception 다시 만들기 - InvalidEmailException
    if (!email.matches(EMAIL_PATTERN)) {
      throw new InvalidPatternException();
    }
    return true;
  }

  public static boolean validateNickname(String nickname) {
    // TODO: exception 다시 만들기 - InvalidNicknameException
    if (!nickname.matches(NICKNAME_PATTERN)) {
      throw new InvalidPatternException();
    }
    return true;
  }
}
