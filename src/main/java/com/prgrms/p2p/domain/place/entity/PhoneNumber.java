package com.prgrms.p2p.domain.place.entity;

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

  private final String PHONE_REGX = "^\\d{2,3}-\\d{3,4}-\\d{4}$";
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
    if (number != null || !number.matches(PHONE_REGX)) {
      throw new IllegalArgumentException();
    }
  }
}
