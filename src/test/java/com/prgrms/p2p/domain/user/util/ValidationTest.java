package com.prgrms.p2p.domain.user.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
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
      String password1 = "test12!!";
      String password2 = "Test1234@";
      String password3 = "1234!@###a";
      // When

      // Then
      assertThat(Validation.validatePassword(password1), is(true));
      assertThat(Validation.validatePassword(password2), is(true));
      assertThat(Validation.validatePassword(password3), is(true));
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
      assertThrows(IllegalArgumentException.class, () -> Validation.validatePassword(password5));
      assertThrows(IllegalArgumentException.class, () -> Validation.validatePassword(password6));
    }
  }

  @Nested
  @DisplayName("닉네임 정규표현식 테스트")
  class testNickname {

    @Test
    @DisplayName("성공 테스트")
    void successNickname() {

      // Given
      List<String> nicknames = new ArrayList<>();
      nicknames.add("test");
      nicknames.add("Test12");
      nicknames.add("12345");
      nicknames.add("!!!@@#");
      nicknames.add("범석범석");
      nicknames.add("범석범석12");
      nicknames.add("범석12!");
      // When

      // Then
      for (String nickname : nicknames) {
        assertThat(Validation.validateNickname(nickname), is(true));
      }
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