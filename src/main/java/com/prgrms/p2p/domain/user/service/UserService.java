package com.prgrms.p2p.domain.user.service;

import static com.prgrms.p2p.domain.user.config.security.JwtExpirationEnum.BAN_EXPIRATION_TIME;

import com.prgrms.p2p.domain.user.config.security.JwtTokenProvider;
import com.prgrms.p2p.domain.user.dto.ChangeProfileResponse;
import com.prgrms.p2p.domain.user.dto.LoginResponse;
import com.prgrms.p2p.domain.user.dto.OtherUserDetailResponse;
import com.prgrms.p2p.domain.user.dto.SignUpRequest;
import com.prgrms.p2p.domain.user.dto.UserDetailResponse;
import com.prgrms.p2p.domain.user.entity.Sex;
import com.prgrms.p2p.domain.user.entity.User;
import com.prgrms.p2p.domain.user.exception.EmailConflictException;
import com.prgrms.p2p.domain.user.exception.LoginFailException;
import com.prgrms.p2p.domain.user.exception.NicknameConflictException;
import com.prgrms.p2p.domain.user.exception.PwdConflictException;
import com.prgrms.p2p.domain.user.exception.UserNotFoundException;
import com.prgrms.p2p.domain.user.exception.WrongInfoException;
import com.prgrms.p2p.domain.user.repository.UserRepository;
import com.prgrms.p2p.domain.user.util.GenerateCertPassword;
import com.prgrms.p2p.domain.user.util.MailBodyUtil;
import com.prgrms.p2p.domain.user.util.PasswordEncrypter;
import com.prgrms.p2p.domain.user.util.UserConverter;
import com.prgrms.p2p.domain.user.util.Validation;
import java.util.Date;
import java.util.Optional;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@PropertySource("classpath:application.yaml")
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final JwtTokenProvider jwtTokenProvider;
  private final RedisTemplate redisTemplate;
  private final UserFacadeService userFacadeService;
  private final JavaMailSender mailSender;
  private final CertNumberService certNumberService;

  @Value("${spring.mail.username}")
  private String sendEmail;

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
  public String postEmail(String email) {

    User user = userRepository.findByEmail(email)
        .orElseThrow(UserNotFoundException::new);

    String temporaryPwd = GenerateCertPassword.generatePassword();
    String certPwd = certNumberService.generateCertNumber(email);

    String fromMail = sendEmail;
    String toMail = email;
    String title = MailBodyUtil.getMailTitle(user.getNickname());
    String content = MailBodyUtil.getMailBody(user.getNickname(), certPwd);

    try {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "utf-8");

      messageHelper.setFrom(fromMail);
      messageHelper.setTo(toMail);
      messageHelper.setSubject(title);
      messageHelper.setText(content, true);

      mailSender.send(message);

    } catch (MessagingException e) {
      e.printStackTrace();
    }

    return certPwd;
  }

  @Transactional
  public void modify(Long userId, String nickname, String birth, Sex sex){
    User user = userRepository.findById(userId)
        .orElseThrow(UserNotFoundException::new);

    //TODO: 변경요청이 된 닉네임이 전과 동일하면 그대로 넣어달라는 그루님 요청
    if(!user.getNickname().equals(nickname)){
      validateNickname(nickname);
    }

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
    if(!user.matchPassword(oldPassword)) throw new WrongInfoException();
    if(user.matchPassword(newPassword)) throw new PwdConflictException();
    user.changePassword(PasswordEncrypter.encrypt(newPassword));

    userRepository.save(user);
  }

  @Transactional
  public void changePassword(String email, String newPassword){
    User user = userRepository.findByEmail(email)
        .orElseThrow(UserNotFoundException::new);

    Validation.validatePassword(newPassword);
    if(user.matchPassword(newPassword)) throw new PwdConflictException();
    user.changePassword(PasswordEncrypter.encrypt(newPassword));

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
