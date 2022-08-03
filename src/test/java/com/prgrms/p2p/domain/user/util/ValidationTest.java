package com.prgrms.p2p.domain.user.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

@DisplayName("정규표현식 테스트")
class ValidationTest {

  @Nested
  @DisplayName("이메일 정규표현식 테스트")
  class testEmail {

    @Test
    @DisplayName("성공 테스트")
    void successEmail() {

      // Given
      String email1 = "qjatjr@gmail.com";
      String email2 = "qjatjr29@gmail.com";
      // When

      // Then
      assertThat(Validation.validateEmail(email1), is(true));
      assertThat(Validation.validateEmail(email2), is(true));
    }

    @Test
    @DisplayName("실패 - 이메일 정규표현식 만족하지 않음")
    void failEmail() {

      // Given
      String email1 = "qjatjr@gmailcom";
      String email2 = "qjatjrgmail.com";
      String email3 = "@gmail.com";
      String email4 = "qjatjrgmail.com";
      // When

      // Then
      assertThrows(IllegalArgumentException.class, () -> Validation.validateEmail(email1));
      assertThrows(IllegalArgumentException.class, () -> Validation.validateEmail(email2));
      assertThrows(IllegalArgumentException.class, () -> Validation.validateEmail(email3));
      assertThrows(IllegalArgumentException.class, () -> Validation.validateEmail(email4));
    }
  }

  @Nested
  @DisplayName("패스워드 정규표현식 테스트")
  class testPassword {

    @Test
    @DisplayName("성공 테스트")
    void successPassword() {

      // Given
      String password1= "test1234!";
      String password2= "Test1234";
      // When

      // Then
      assertThat(Validation.validatePassword(password1), is(true));
      assertThat(Validation.validatePassword(password2), is(true));
    }

    @Test
    @DisplayName("실패 - 패스워드 정규표현식 만족하지 않음")
    void failPassword() {

      // Given
      String password1 = "test123";
      String password2 = "test12345678910!";
      String password3 = "testtest";
      String password4 = "123456789";
      String password5 = "123456789!";
      String password6 = "testtest!";
      // When

      // Then
      assertThrows(IllegalArgumentException.class, () -> Validation.validatePassword(password1));
      assertThrows(IllegalArgumentException.class, () -> Validation.validatePassword(password2));
      assertThrows(IllegalArgumentException.class, () -> Validation.validatePassword(password3));
      assertThrows(IllegalArgumentException.class, () -> Validation.validatePassword(password4));
    }
  }

  @Nested
  @DisplayName("닉네임 정규표현식 테스트")
  class testNickname {

    @Test
    @DisplayName("성공 테스트")
    void successNickname() {

      // Given
      String nickname1 = "test";
      String nickname2 = "Test12";
      String nickname3 = "12345";
      String nickname4 = "!!!@@#";
      // When

      // Then
      assertThat(Validation.validateNickname(nickname1), is(true));
      assertThat(Validation.validateNickname(nickname2), is(true));
      assertThat(Validation.validateNickname(nickname3), is(true));
      assertThat(Validation.validateNickname(nickname4), is(true));
    }

    @Test
    @DisplayName("실패 - 닉네임 정규표현식 만족하지 않음")
    void failNickname() {

      // Given
      String nickname1 = "test123";
      String nickname2 = "te";
      String nickname3 = "test123!";
      // When

      // Then
      assertThrows(IllegalArgumentException.class, () -> Validation.validateNickname(nickname1));
      assertThrows(IllegalArgumentException.class, () -> Validation.validateNickname(nickname2));
      assertThrows(IllegalArgumentException.class, () -> Validation.validateNickname(nickname3));
    }
  }
}