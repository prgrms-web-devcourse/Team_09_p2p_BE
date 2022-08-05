package com.prgrms.p2p.domain.course.entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Period {
  ONE_DAY("당일"), ONE_THREE_DAYS("1~3일"), FOUR_SEVEN_DAYS("4~7일"), EIGHT_FIFTEENTH(
      "8~15일"), MORE_THAN_FIFTEENTH("15일이상");

  private final String value;

  Period(String value) {
    this.value = value;
  }

  public static Period from(String value) {
    for (Period period : Period.values()) {
      if (period.getValue().equals(value)) {
        return period;
      }
    }
    return null;
  }

  @JsonValue
  public String getValue() {
    return value;
  }
}
