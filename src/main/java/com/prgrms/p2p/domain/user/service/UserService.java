package com.prgrms.p2p.domain.user.service;

import static com.prgrms.p2p.domain.user.config.security.JwtExpirationEnum.BAN_EXPIRATION_TIME;

import com.prgrms.p2p.domain.user.config.security.JwtTokenProvider;
import com.prgrms.p2p.domain.user.dto.LoginResponse;
import com.prgrms.p2p.domain.user.dto.SignUpRequest;
import com.prgrms.p2p.domain.user.dto.UserDetailResponse;
import com.prgrms.p2p.domain.user.entity.User;
import com.prgrms.p2p.domain.user.repository.UserRepository;
import com.prgrms.p2p.domain.user.util.UserConverter;
import com.prgrms.p2p.domain.user.util.Validation;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {

  private final UserRepository userRepository;
  private final JwtTokenProvider jwtTokenProvider;
  private final RedisTemplate redisTemplate;

  public UserService(UserRepository userRepository,
      JwtTokenProvider jwtTokenProvider,
      RedisTemplate redisTemplate) {
    this.userRepository = userRepository;
    this.jwtTokenProvider = jwtTokenProvider;
    this.redisTemplate = redisTemplate;
  }

  @Transactional
  public String signUp(SignUpRequest signUpRequest) {

    validatePassword(signUpRequest.getPassword(), signUpRequest.getPasswordCheck());
    validateNickname(signUpRequest.getNickname());
    validateEmail(signUpRequest.getEmail());

    User user = userRepository.save(UserConverter.toUser(signUpRequest));
    return user.getNickname();
  }

  @Transactional
  public Optional<LoginResponse> login(String email, String password) {

    //래디스 체크
    redisTemplate.opsForValue().increment(email);

    if((int)redisTemplate.opsForValue().get(email) >= 5){
      redisTemplate.opsForValue().set(email, 5, BAN_EXPIRATION_TIME.getValue(), TimeUnit.MILLISECONDS);
    }


    //TODO : 알맞는 예외 설정해주기 - NotFoundException
    //이메일 존재유무 확인
    User user = userRepository.findByEmail(email)
        .orElseThrow(RuntimeException::new);

    //비밀번호 확인
    user.matchPassword(password);

    //성공적인 로그인시 생성했던 레디스 삭제
    redisTemplate.delete(email);

    //토큰 생성및 LoginResponse 변환
    return Optional.of(user).map((u) ->
        UserConverter.fromUserAndToken(u, jwtTokenProvider.generateAccessToken(user.getId(), user.getEmail())));
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
