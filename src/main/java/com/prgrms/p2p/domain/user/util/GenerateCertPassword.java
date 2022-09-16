package com.prgrms.p2p.domain.user.util;

import java.util.Random;

public class GenerateCertPassword {

  private static final char[] alphaTable = {
      'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
      'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
      'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
      'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
      'w', 'x', 'y', 'z'
  };

  private static final char[] specialCharTable = {
      '!', '@', '#', '$', '%', '^', '&', '*'
  };

  private static final char[] numberTable = {
    '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'
  };

  public static String generatePassword() {

    Random random = new Random();

    int alphaLength = alphaTable.length;
    int specialLength = specialCharTable.length;
    int numberLength = numberTable.length;

    int pwdLength = ((int) (Math.random() * 7)) + 8;

    int pwdSpecialLength = ((int) (Math.random() * 2)) + 1;
    int pwdNumberLength = ((int) (Math.random() * (pwdLength - pwdSpecialLength - 1))) + 1;
    int pwdAlphaLength = pwdLength - pwdSpecialLength - pwdNumberLength - 1;

    StringBuilder pwdBuilder = new StringBuilder();

    for(int i = 0; i < pwdAlphaLength; i++) {
      pwdBuilder.append(alphaTable[random.nextInt(alphaLength)]);
    }

    for(int i = 0; i < pwdNumberLength; i++) {
      pwdBuilder.append(numberTable[random.nextInt(numberLength)]);
    }

    for(int i = 0; i < pwdSpecialLength; i++) {
      pwdBuilder.append(specialCharTable[random.nextInt(specialLength)]);
    }

    return pwdBuilder.toString();
  }

}
