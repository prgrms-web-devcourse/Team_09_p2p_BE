package com.prgrms.p2p.domain.user.service;

import com.prgrms.p2p.domain.user.config.security.JwtExpirationEnum;
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

    Validation.validatePassword(signUpRequest.getPassword());
    Validation.validatePassword(signUpRequest.getEmail());

    User user = userRepository.save(UserConverter.toUser(signUpRequest));
    return user.getNickname();
  }

  @Transactional
  public Optional<LoginResponse> login(String email, String password) {

    //TODO : 알맞는 예외 설정해주기 - NotFoundException
    //이메일 존재유무 확인
    User user = userRepository.findByEmail(email)
        .orElseThrow(RuntimeException::new);

    //비밀번호 확인
    user.matchPassword(password);

    String accessToken = jwtTokenProvider.generateAccessToken(email);

    //래디스 추가
    //TODO : 유효시간 가져올 방법
    redisTemplate.opsForValue()
        .set(email,accessToken , JwtExpirationEnum.ACCESS_TOKEN_EXPIRATION_TIME.getValue(),TimeUnit.MILLISECONDS);

    //토큰 생성및 LoginResponse 변환
    return Optional.of(user).map((u) ->
        UserConverter.fromUserAndToken(u, accessToken));
  }

  public UserDetailResponse getUserInfo(Long userId) {

    // TODO: NotFoundException 만들어주기
    User user = userRepository.findById(userId)
        .orElseThrow(IllegalArgumentException::new);

    return UserConverter.detailFromUser(user);
  }

  //TODO: Exception 만들기
  public void validateEmail(String email) {
    userRepository.findByEmail(email)
        .ifPresent((s) -> {
          throw new IllegalArgumentException();
        });
  }

  public void validateNickname(String nickname) {
    userRepository.findByNickname(nickname)
        .ifPresent((s) -> {
          throw new IllegalArgumentException();
        });
  }
}
