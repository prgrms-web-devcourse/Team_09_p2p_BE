package com.prgrms.p2p.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.prgrms.p2p.domain.user.dto.LoginResponse;
import com.prgrms.p2p.domain.user.dto.OtherUserDetailResponse;
import com.prgrms.p2p.domain.user.dto.SignUpRequest;
import com.prgrms.p2p.domain.user.dto.UserDetailResponse;
import com.prgrms.p2p.domain.user.entity.Sex;
import com.prgrms.p2p.domain.user.entity.User;
import com.prgrms.p2p.domain.user.exception.InvalidPatternException;
import com.prgrms.p2p.domain.user.exception.PwdConflictException;
import com.prgrms.p2p.domain.user.exception.UserNotFoundException;
import com.prgrms.p2p.domain.user.exception.WrongInfoException;
import com.prgrms.p2p.domain.user.repository.UserRepository;
import java.util.Optional;
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

@SpringBootTest
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
        "test1234!",
        "test1234!",
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
      signUpRequest.setPasswordCheck("test1235!");
      // Then
      assertThrows(IllegalArgumentException.class, () -> userService.signUp(signUpRequest));
    }

    @Test
    @DisplayName("실패 - password 형식이 다른 경우")
    void failValidatePassword() {
      // Given
      // When
      signUpRequest.setPasswordCheck("test1235!!!!!!!!!!!!!");
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
      signUpRequest.setNickname("beomsicgoodis");
      // Then
      assertThrows(IllegalArgumentException.class, () -> userService.signUp(signUpRequest));
    }

    @Test
    @DisplayName("실패 - 이미 같은 nickname이 있는 경우")
    void failAlreadyExistNickname() {
      // Given
      SignUpRequest newRequest = new SignUpRequest("beomsic@gmail.com",
          "test1245!",
          "test1245!",
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
          "test1245!",
          "test1245!",
          "beom12",
          "1997-11-29",
          Sex.MALE);
      // When
      userService.signUp(signUpRequest);
      // Then
      assertThrows(IllegalArgumentException.class, () -> userService.signUp(newRequest));
    }
  }

  @Nested
  @DisplayName("본인 상세정보 조회 테스트")
  class getUserInfoTest {

    @Test
    @DisplayName("성공 테스트")
    void success() {
      String nickname = userService.signUp(signUpRequest);

      User user = userRepository.findByNickname(nickname).orElseThrow(IllegalArgumentException::new);

      UserDetailResponse userInfo = userService.getUserInfo(user.getId());

      assertThat(userInfo.getId()).isEqualTo(user.getId());
      assertThat(userInfo.getEmail()).isEqualTo(user.getEmail());
      assertThat(userInfo.getNickname()).isEqualTo(user.getNickname());
      assertThat(userInfo.getProfileImage()).isEqualTo(null);
      assertThat(userInfo.getBirth()).isEqualTo(user.getBirth().toString());
      assertThat(userInfo.getSex()).isEqualTo(user.getSex());
    }

    @Test
    @DisplayName("실패 - 존재하지 않는 Id 입력")
    void failNotExistId() {

      assertThatThrownBy(() -> userService.getUserInfo(100L))
          .isInstanceOf(IllegalArgumentException.class);
    }
  }

  @Nested
  @DisplayName("타인 상세정보 조회 테스트")
  class getOtherInfoTest {

    @Test
    @DisplayName("성공 테스트")
    void success() {
      String nickname = userService.signUp(signUpRequest);

      User user = userRepository.findByNickname(nickname).orElseThrow(IllegalArgumentException::new);

      OtherUserDetailResponse otherInfo = userService.getOtherInfo(user.getId());

      assertThat(otherInfo.getId()).isEqualTo(user.getId());
      assertThat(otherInfo.getNickname()).isEqualTo(user.getNickname());
      assertThat(otherInfo.getProfileImage()).isEqualTo(null);
      assertThat(otherInfo.getBirth()).isEqualTo(user.getBirth().toString());
      assertThat(otherInfo.getSex()).isEqualTo(user.getSex());
    }

    @Test
    @DisplayName("실패 - 존재하지 않는 Id 입력")
    void failNotExistId() {

      assertThatThrownBy(() -> userService.getUserInfo(100L))
          .isInstanceOf(IllegalArgumentException.class);
    }
  }

  @Nested
  @DisplayName("회원 탈퇴 테스트")
  class deleteTest {

    @Test
    @DisplayName("성공 테스트")
    void success() {
      String nickname = userService.signUp(signUpRequest);

      User user = userRepository.findByNickname(nickname).orElseThrow(IllegalArgumentException::new);

      userService.delete(user.getId());
      Optional<User> target = userRepository.findById(user.getId());
      assertThat(target).isEmpty();
    }

    @Test
    @DisplayName("실패 - 존재하지 않는 Id 입력")
    void failNotExistId() {
      assertThatThrownBy(() -> userService.delete(100L))
          .isInstanceOf(IllegalArgumentException.class);
    }
  }

  @Nested
  @DisplayName("비밀번호 변경 테스트")
  class changePasswordTest {

    @Test
    @DisplayName("성공 테스트")
    void success() {
      String nickname = userService.signUp(signUpRequest);
      User user = userRepository.findByNickname(nickname).orElseThrow(IllegalArgumentException::new);
      userService.changePassword(user.getId(),"test1234!", "change1234!");

      assertThat(user.getPassword()).isEqualTo("change1234!");
    }

    @Test
    @DisplayName("실패 - 잘못된 비밀번호 입력")
    void failWrongPassword() {
      String nickname = userService.signUp(signUpRequest);
      User user = userRepository.findByNickname(nickname).orElseThrow(UserNotFoundException::new);

      assertThatThrownBy(() -> userService.changePassword(user.getId(),"wrong1234", "change1234!"))
          .isInstanceOf(WrongInfoException.class);
    }

    @Test
    @DisplayName("실패 - 새로운 비밀번호 형식이 다른 경우")
    void failValidatePassword() {
      String nickname = userService.signUp(signUpRequest);
      User user = userRepository.findByNickname(nickname).orElseThrow(UserNotFoundException::new);

      assertThatThrownBy(() -> userService.changePassword(user.getId(),user.getPassword(), "abcd"))
          .isInstanceOf(InvalidPatternException.class);
      assertThatThrownBy(() -> userService.changePassword(user.getId(),user.getPassword(), "   "))
          .isInstanceOf(InvalidPatternException.class);
    }

    @Test
    @DisplayName("실패 - 변경하려는 패스워드가 현재 비밀번호와 같은 경우")
    void failExistPassword() {
      // Given

      // When
      String nickname = userService.signUp(signUpRequest);
      User user = userRepository.findByNickname(nickname)
          .orElseThrow(UserNotFoundException::new);

      String oldPassword = "test1234!";
      String newPassword = "test1234!";

      // Then
      System.out.println(user.getPassword());
      assertThatThrownBy(() -> userService.changePassword(user.getId(), oldPassword, newPassword))
          .isInstanceOf(PwdConflictException.class);
    }
  }

  @Nested
  @DisplayName("회원 정보 수정 테스트")
  class modifyTest {

    @Test
    @DisplayName("성공 테스트")
    void success() {
      String nickname = userService.signUp(signUpRequest);

      User user = userRepository.findByNickname(nickname).orElseThrow(IllegalArgumentException::new);
      userService.modify(user.getId(), "KATE","1999-11-29",Sex.FEMALE);
      User target = userRepository.findById(user.getId()).orElseThrow(IllegalArgumentException::new);

      assertThat(target.getId()).isEqualTo(user.getId());
      assertThat(target.getNickname()).isEqualTo("KATE");
      assertThat(target.getBirth()).isEqualTo("1999-11-29");
      assertThat(target.getSex()).isEqualTo(Sex.FEMALE);

    }

    @Test
    @DisplayName("실패 - 빈 닉네임 입력")
    void failBlankNickname() {
      String nickname = userService.signUp(signUpRequest);

      User user = userRepository.findByNickname(nickname).orElseThrow(IllegalArgumentException::new);

      assertThatThrownBy(() -> userService.modify(user.getId(), "  ","1999-11-29",Sex.FEMALE))
          .isInstanceOf(RuntimeException.class);

    }

    @Test
    @DisplayName("실패 - 빈 생일 입력")
    void failBlankBirth() {
      String nickname = userService.signUp(signUpRequest);

      User user = userRepository.findByNickname(nickname).orElseThrow(IllegalArgumentException::new);

      assertThatThrownBy(() -> userService.modify(user.getId(), "KATE","  ",Sex.FEMALE))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("실패 - 성별 null 입력")
    void failNullSex() {
      String nickname = userService.signUp(signUpRequest);

      User user = userRepository.findByNickname(nickname).orElseThrow(IllegalArgumentException::new);

      assertThatThrownBy(() -> userService.modify(user.getId(), "KATE","1999-11-29",null))
          .isInstanceOf(RuntimeException.class);
    }
  }

  @Nested
  @DisplayName("유저 프로필 변경 테스트")
  class changeProfileUrlTest {

    @Test
    @DisplayName("성공 테스트")
    void success() {
      String nickname = userService.signUp(signUpRequest);
      User user = userRepository.findByNickname(nickname).orElseThrow(IllegalArgumentException::new);
      userService.changeProfileUrl(user.getId(), "changeurl");

      assertThat(user.getProfileUrl().get()).isEqualTo("changeurl");
    }

    @Test
    @DisplayName("실패 - 존재하지 않는 Id 입력")
    void failNotExistId() {
      String nickname = userService.signUp(signUpRequest);
      User user = userRepository.findByNickname(nickname).orElseThrow(IllegalArgumentException::new);

      assertThatThrownBy(() -> userService.changeProfileUrl(100L, "changeurl"))
          .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("실패 - profileurl 이 비어있는 경우")
    void failWrongProfileUrl() {
      String nickname = userService.signUp(signUpRequest);
      User user = userRepository.findByNickname(nickname).orElseThrow(IllegalArgumentException::new);

      assertThatThrownBy(() -> userService.changeProfileUrl(user.getId(), "  "))
          .isInstanceOf(RuntimeException.class);

    }
  }

  @Nested
  @DisplayName("로그인 테스트")
  class loginTest {

    @Test
    @DisplayName("성공 테스트")
    void success() {
      String nickname = userService.signUp(signUpRequest);
      User user = userRepository.findByNickname(nickname).orElseThrow(IllegalArgumentException::new);

      LoginResponse login = userService.login(user.getEmail(), "test1234!").orElseThrow(IllegalArgumentException::new);

      assertThat(login.getUser().getId()).isEqualTo(user.getId());
      assertThat(login.getUser().getNickname()).isEqualTo(user.getNickname());
      assertThat(login.getUser().getProfileImage()).isEqualTo(null);

    }

    @Test
    @DisplayName("실패 - 존재하지 않는 email 입력")
    void failNotExistEmail() {
      String nickname = userService.signUp(signUpRequest);

      assertThatThrownBy(() -> userService.login("kim1234@gmail.com", "test1234!"))
          .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("실패 - 잘못된 패스워스 인 경우")
    void failWrongPassword() {
      String nickname = userService.signUp(signUpRequest);
      User user = userRepository.findByNickname(nickname).orElseThrow(IllegalArgumentException::new);

      assertThatThrownBy(() -> userService.login(user.getEmail(), "wrong1234!"))
          .isInstanceOf(IllegalArgumentException.class);

    }
  }
}
