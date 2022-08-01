package com.prgrms.p2p.domain.user.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

import com.prgrms.p2p.domain.user.dto.SignUpRequest;
import com.prgrms.p2p.domain.user.entity.Sex;

import com.prgrms.p2p.domain.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional(readOnly = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@DisplayName("유저 서비스 테스트")
class UserServiceTest {

  @Autowired
  private UserService userService;

  @Autowired
  private UserRepository userRepository;

  private SignUpRequest signUpRequest;

  @BeforeEach
  void setup() {
    signUpRequest = new SignUpRequest("test@gmail.com",
        "test1234",
        "test1234",
        "beom",
        "1997-11-29",
        Sex.MALE);
  }

  @AfterEach
  void clear() {
    userRepository.deleteAll();
  }

  //TODO: 예외들 다 바꿔줘야함
  @Nested
  @DisplayName("사용자 회원가입 테스트")
  class signUpTest {

    @Test
    @DisplayName("성공 테스트")
    void success() {
      // Given
      String nickname = userService.signUp(signUpRequest);
      // When
      // Then
      assertThat(nickname.equals("beom"), is(true));
    }

    @Test
    @DisplayName("실패 - password와 passwordCheck가 다른 경우")
    void failNotMatchPassword() {
      // Given
      // When
      signUpRequest.setPasswordCheck("test1235");
      // Then
      assertThrows(IllegalArgumentException.class, () -> userService.signUp(signUpRequest));
    }

    @Test
    @DisplayName("실패 - password 형식이 다른 경우")
    void failValidatePassword() {
      // Given
      // When
      signUpRequest.setPasswordCheck("test1235!!!");
      // Then
      assertThrows(IllegalArgumentException.class, () -> userService.signUp(signUpRequest));
    }

    @Test
    @DisplayName("실패 - email 형식이 다른 경우")
    void failValidateEmail() {
      // Given
      // When
      signUpRequest.setEmail("test@gmailcom");
      // Then
      assertThrows(IllegalArgumentException.class, () -> userService.signUp(signUpRequest));
    }

    @Test
    @DisplayName("실패 - nickname 형식이 다른 경우")
    void failValidateNickname() {
      // Given
      // When
      signUpRequest.setNickname("beomsic");
      // Then
      assertThrows(IllegalArgumentException.class, () -> userService.signUp(signUpRequest));
    }

    @Test
    @DisplayName("실패 - 이미 같은 nickname이 있는 경우")
    void failAlreadyExistNickname() {
      // Given
      SignUpRequest newRequest = new SignUpRequest("beomsic@gmail.com",
          "test1245",
          "test1245",
          "beom",
          "1997-11-29",
          Sex.MALE);
      // When
      userService.signUp(signUpRequest);
      // Then
      assertThrows(IllegalArgumentException.class, () -> userService.signUp(newRequest));
    }

    @Test
    @DisplayName("실패 - 이미 같은 email이 있는 경우")
    void failAlreadyExistEmail() {
      // Given
      SignUpRequest newRequest = new SignUpRequest("test@gmail.com",
          "test1245",
          "test1245",
          "beom12",
          "1997-11-29",
          Sex.MALE);
      // When
      userService.signUp(signUpRequest);
      // Then
      assertThrows(IllegalArgumentException.class, () -> userService.signUp(newRequest));
    }
  }

}