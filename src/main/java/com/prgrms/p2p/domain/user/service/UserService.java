package com.prgrms.p2p.domain.user.service;

import static com.prgrms.p2p.domain.user.config.security.JwtExpirationEnum.BAN_EXPIRATION_TIME;

import com.prgrms.p2p.domain.bookmark.service.CourseBookmarkService;
import com.prgrms.p2p.domain.bookmark.service.PlaceBookmarkService;
import com.prgrms.p2p.domain.like.service.CourseLikeService;
import com.prgrms.p2p.domain.like.service.PlaceLikeService;
import com.prgrms.p2p.domain.user.config.security.JwtTokenProvider;
import com.prgrms.p2p.domain.user.dto.ChangeProfileResponse;
import com.prgrms.p2p.domain.user.dto.LoginResponse;
import com.prgrms.p2p.domain.user.dto.OtherUserDetailResponse;
import com.prgrms.p2p.domain.user.dto.SignUpRequest;
import com.prgrms.p2p.domain.user.dto.UserDetailResponse;
import com.prgrms.p2p.domain.user.dto.UserLikeResponse;
import com.prgrms.p2p.domain.user.entity.Sex;
import com.prgrms.p2p.domain.user.entity.User;
import com.prgrms.p2p.domain.user.exception.EmailConflictException;
import com.prgrms.p2p.domain.user.exception.LoginFailException;
import com.prgrms.p2p.domain.user.exception.NicknameConflictException;
import com.prgrms.p2p.domain.user.exception.PwdConflictException;
import com.prgrms.p2p.domain.user.exception.UserNotFoundException;
import com.prgrms.p2p.domain.user.repository.UserRepository;
import com.prgrms.p2p.domain.user.util.UserConverter;
import com.prgrms.p2p.domain.user.util.Validation;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.joda.time.LocalDateTime;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final JwtTokenProvider jwtTokenProvider;
  private final RedisTemplate redisTemplate;
  private final UserFacadeService userFacadeService;


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

    User user = userRepository.findByEmail(email)
        .orElseThrow(UserNotFoundException::new);

    Object expiredAt = redisTemplate.opsForHash().get(email, "expiredAt");
    Long count = redisTemplate.opsForHash().increment(email, "count", 1);

    if(count >= 5){
      redisTemplate.delete(email);
      HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();

      String banExpiredAt = String.valueOf(
          LocalDateTime.fromDateFields(
              new Date(new Date().getTime() + BAN_EXPIRATION_TIME.getValue())));

      hash.put(email, "count", "5");
      hash.put(email, "expiredAt", banExpiredAt);

      throw new LoginFailException(5, banExpiredAt);
    }

    if(!user.matchPassword(password)) {
      throw new LoginFailException(Math.toIntExact(count), String.valueOf(expiredAt));
    }
    redisTemplate.delete(email);

    return Optional.of(user).map((u) ->
        UserConverter.fromUserAndToken(u, jwtTokenProvider.generateAccessToken(user.getId(), user.getEmail())));
  }

  @Transactional
  public void modify(Long userId, String nickname, String birth, Sex sex){
    User user = userRepository.findById(userId)
        .orElseThrow(UserNotFoundException::new);

    validateNickname(nickname);
    user.changeNickname(nickname);
    user.changeBirth(birth);
    user.changeSex(sex);

    userRepository.save(user);
  }

  @Transactional
  public void changePassword(Long userId, String oldPassword, String newPassword){
    User user = userRepository.findById(userId)
        .orElseThrow(UserNotFoundException::new);

    Validation.validatePassword(newPassword);
    if(!user.matchPassword(oldPassword)) throw new PwdConflictException();
    user.changePassword(newPassword);

    userRepository.save(user);
  }

  @Transactional
  public ChangeProfileResponse changeProfileUrl(Long userId, String profileUrl) {
    User user = userRepository.findById(userId)
        .orElseThrow(UserNotFoundException::new);

    user.changeProfileUrl(profileUrl);
    userRepository.save(user);
    return new ChangeProfileResponse(profileUrl);
  }

  @Transactional
  public void delete(Long userId){

    User user = userRepository.findById(userId)
        .orElseThrow(UserNotFoundException::new);

    userRepository.delete(user);
  }

  public UserDetailResponse getUserInfo(Long userId) {

    User user = userRepository.findById(userId)
        .orElseThrow(UserNotFoundException::new);

    return userFacadeService.getInfo(user);
  }

  public OtherUserDetailResponse getOtherInfo(Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(UserNotFoundException::new);

    return userFacadeService.getOtherInfo(user);
  }

  public void validateEmail(String email) {
    Validation.validateEmail(email);
    userRepository.findByEmail(email)
        .ifPresent((s) -> {
          throw new EmailConflictException();
        });
  }

  public void validateNickname(String nickname) {
    Validation.validateNickname(nickname);
    userRepository.findByNickname(nickname)
        .ifPresent((s) -> {
          throw new NicknameConflictException();
        });
  }

  private void validatePassword(String password, String passwordCheck) {
    Validation.validatePassword(password);
    if(!password.equals(passwordCheck)) throw new PwdConflictException();
  }
}
