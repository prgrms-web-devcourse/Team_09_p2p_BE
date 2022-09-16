package com.prgrms.p2p.domain.user.service;

import com.prgrms.p2p.domain.common.exception.BadRequestException;
import com.prgrms.p2p.domain.common.exception.NotFoundException;
import java.util.Objects;
import java.util.Random;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class CertNumberService {

  private final RedisTemplate redisTemplate;

  private static final char[] alphaTable = {
      'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
      'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
      'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
      'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
      'w', 'x', 'y', 'z'
  };

  private static final char[] specialCharTable = {
      '!', '@', '#', '$', '%', '^', '&', '*'
  };

  private static final char[] numberTable = {
      '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'
  };

  public CertNumberService(RedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  public String generatePassword() {

    Random random = new Random();

    int alphaLength = alphaTable.length;
    int specialLength = specialCharTable.length;
    int numberLength = numberTable.length;

    int pwdLength = ((int) (Math.random() * 7)) + 8;

    int pwdSpecialLength = ((int) (Math.random() * 2)) + 1;
    int pwdNumberLength = ((int) (Math.random() * (pwdLength - pwdSpecialLength - 1))) + 1;
    int pwdAlphaLength = pwdLength - pwdSpecialLength - pwdNumberLength - 1;

    StringBuilder pwdBuilder = new StringBuilder();

    for(int i = 0; i < pwdAlphaLength; i++) {
      pwdBuilder.append(alphaTable[random.nextInt(alphaLength)]);
    }

    for(int i = 0; i < pwdNumberLength; i++) {
      pwdBuilder.append(numberTable[random.nextInt(numberLength)]);
    }

    for(int i = 0; i < pwdSpecialLength; i++) {
      pwdBuilder.append(specialCharTable[random.nextInt(specialLength)]);
    }

    return pwdBuilder.toString();
  }

  @Transactional
  public String generateCertNumber(String email) {

    Random random = new Random();
    int length = numberTable.length;

    StringBuilder certNumberBuilder = new StringBuilder();

    for(int i = 0; i < 6; i++) {
      certNumberBuilder.append(numberTable[random.nextInt(length)]);
    }

    String certNumber = certNumberBuilder.toString();

    ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

    valueOperations.set(email, certNumber);

    return certNumber;
  }

  public void matchCertNumber(String email, String number) {

    ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

    if(Boolean.FALSE.equals(redisTemplate.hasKey(email))) throw new NotFoundException("이미 제한시간이 지났거나 없는 이메일입니다.");

    if(!Objects.equals(valueOperations.get(email), number)) throw new BadRequestException("인증에 실패했습니다.");
  }

  @Transactional
  public void deleteCertNumber(String email) {
    ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
    valueOperations.getAndDelete(email);
  }

}
