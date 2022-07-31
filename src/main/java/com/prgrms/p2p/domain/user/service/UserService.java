package com.prgrms.p2p.domain.user.service;

import com.prgrms.p2p.domain.user.dto.SignUpRequest;
import com.prgrms.p2p.domain.user.dto.UserDetailResponse;
import com.prgrms.p2p.domain.user.entity.User;
import com.prgrms.p2p.domain.user.repository.UserRepository;
import com.prgrms.p2p.domain.user.util.UserConverter;
import com.prgrms.p2p.domain.user.util.Validation;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional
  public String signUp(SignUpRequest signUpRequest) {

    validatePassword(signUpRequest.getPassword(), signUpRequest.getPasswordCheck());
    validateNickname(signUpRequest.getNickname());
    validateEmail(signUpRequest.getEmail());

    User user = userRepository.save(UserConverter.toUser(signUpRequest));
    return user.getNickname();
  }

  public UserDetailResponse getUserInfo(Long userId) {

    // TODO: NotFoundException 만들어주기
    User user = userRepository.findById(userId)
        .orElseThrow(IllegalArgumentException::new);

    return UserConverter.detailFromUser(user);
  }

  //TODO: Exception 만들기
  public void validateEmail(String email) {
    Validation.validateEmail(email);
    userRepository.findByEmail(email)
        .ifPresent((s) -> {
          throw new IllegalArgumentException();
        });
  }

  public void validateNickname(String nickname) {
    Validation.validateNickname(nickname);
    userRepository.findByNickname(nickname)
        .ifPresent((s) -> {
          throw new IllegalArgumentException();
        });
  }

  private void validatePassword(String password, String passwordCheck) {
    Validation.validatePassword(password);
    if(!password.equals(passwordCheck)) throw new IllegalArgumentException("비밀번호가 서로 다릅니다.");
  }
}
