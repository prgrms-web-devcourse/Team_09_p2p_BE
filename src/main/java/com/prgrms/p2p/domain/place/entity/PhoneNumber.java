package com.prgrms.p2p.domain.place.entity;

import com.prgrms.p2p.domain.common.exception.BadRequestException;
import java.util.Objects;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@Access(value = AccessType.FIELD)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class PhoneNumber {

  private String number;

  public PhoneNumber(String number) {
    validationPhone(number);
    this.number = number;
  }

  public static PhoneNumber of(String number) {
    return new PhoneNumber(number);
  }

  public String getNumber() {
    return number;
  }

  public void validationPhone(String number) {
    if (Objects.isNull(number) || !number.matches("^\\d{2,3}-\\d{3,4}-\\d{4}$")) {
      throw new BadRequestException("입력값이 전화번호 형식에 맞지 않습니다.");
    }
  }
}
