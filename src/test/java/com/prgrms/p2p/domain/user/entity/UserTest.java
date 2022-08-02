package com.prgrms.p2p.domain.user.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


class UserTest {

  @Nested
  @DisplayName("유저 생성 테스트")
  class SaveTest {

    @Test
    @DisplayName("성공: 유저 생성에 성공합니다.")
    public void success() throws Exception {

      // Given
      User user = new User("test@gmail.com",
          "password12!",
          "beomsic",
          "1997-11-29",
          Sex.MALE);
      // When
      // Then
      assertThat(user.getEmail()).isEqualTo("test@gmail.com");
      assertThat(user.getPassword()).isEqualTo("password12!");
      assertThat(user.getNickname()).isEqualTo("beomsic");
      assertThat(user.getBirth()).isEqualTo("1997-11-29");
      assertThat(user.getSex()).isEqualTo(Sex.MALE);
    }

    @Test
    @DisplayName("실패: 이메일이 null 일 경우")
    void failNullName() {

      // Given
      // When
      // Then
      assertThrows(RuntimeException.class,
          () -> new User(null,
              "password12!",
              "beom",
              "1997-11-29",
              Sex.MALE));
    }

    @Test
    @DisplayName("실패: 이메일이 empty 일 경우")
    void failEmptyEmail() {

      // Given
      // When
      // Then
      assertThrows(RuntimeException.class,
          () ->new User("",
              "password12!",
              "beom",
              "1997-11-29",
              Sex.MALE));
    }

    @Test
    @DisplayName("실패: 이메일이 blank 일 경우")
    void failBlankEmail() {

      // Given
      // When
      // Then
      assertThrows(RuntimeException.class,
          () ->new User("  ",
              "password12!",
              "beom",
              "1997-11-29",
              Sex.MALE));
    }

    @Test
    @DisplayName("실패: password가 null 일 경우")
    void failNullPassword() {

      // Given
      // When
      // Then
      assertThrows(RuntimeException.class,
          () ->new User("test@gmail.com",
              null,
              "beom",
              "1997-11-29",
              Sex.MALE));
    }

    @Test
    @DisplayName("실패: password가 empty 일 경우")
    void failEmptyPassword() {

      // Given
      // When
      // Then
      assertThrows(RuntimeException.class,
          () ->new User("test@gmail.com",
              "",
              "beom",
              "1997-11-29",
              Sex.MALE));
    }

    @Test
    @DisplayName("실패: password가 Blank 일 경우")
    void failBlankPassword() {

      // Given
      // When
      // Then
      assertThrows(RuntimeException.class,
          () ->new User("test@gmail.com",
              "   ",
              "beom",
              "1997-11-29",
              Sex.MALE));
    }

    @Test
    @DisplayName("실패: 닉네임이 null 일 경우")
    void failNullNickname() {

      // Given
      // When
      // Then
      assertThrows(RuntimeException.class,
          () ->new User("test@gmail.com",
              "test1234",
              null,
              "1997-11-29",
              Sex.MALE));
    }

    @Test
    @DisplayName("실패: 닉네임이 empty 일 경우")
    void failEmptyNickname() {

      // Given
      // When
      // Then
      assertThrows(RuntimeException.class,
          () ->new User("test@gmail.com",
              "test1234",
              "",
              "1997-11-29",
              Sex.MALE));
    }

    @Test
    @DisplayName("실패: 닉네임이 blank 일 경우")
    void failBlankNickname() {

      // Given
      // When
      // Then
      assertThrows(RuntimeException.class,
          () ->new User("test@gmail.com",
              "test1234",
              "    ",
              "1997-11-29",
              Sex.MALE));
    }

    @Test
    @DisplayName("실패: 생일이 null 일 경우")
    void failNullBirth() {

      // Given
      // When
      // Then
      assertThrows(RuntimeException.class,
          () ->new User("test@gmail.com",
              "test1234",
              "beom",
              null,
              Sex.MALE));
    }

    @Test
    @DisplayName("실패: 생일이 empty 일 경우")
    void failEmptyBirth() {

      // Given
      // When
      // Then
      assertThrows(RuntimeException.class,
          () ->new User("test@gmail.com",
              "test1234",
              "beom",
              "",
              Sex.MALE));
    }

    @Test
    @DisplayName("실패: 생일이 blank 일 경우")
    void failBlankBirth() {

      // Given
      // When
      // Then
      assertThrows(RuntimeException.class,
          () ->new User("test@gmail.com",
              "test1234",
              "beom",
              "   ",
              Sex.MALE));
    }

    @Test
    @DisplayName("실패: 성별이 null 일 경우")
    void failNullSex() {

      // Given
      // When
      // Then
      assertThrows(RuntimeException.class,
          () ->new User("test@gmail.com",
              "test1234",
              "beom",
              "1997-11-29",
              null));
    }
  }

}