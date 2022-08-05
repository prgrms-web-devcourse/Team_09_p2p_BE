package com.prgrms.p2p.domain.course.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Period {
  ONE_DAY("당일"), ONE_THREE_DAYS("1~3일"), FOUR_SEVEN_DAYS("4~7일"), EIGHT_FIFTEENTH(
      "8~15일"), MORE_THAN_FIFTEENTH("15일이상");

  private final String value;

  @JsonCreator
  public static Period from(String value) {
    return Arrays.stream(Period.values())
        .filter(period -> period.getValue().equalsIgnoreCase(value)).findFirst().orElse(null);
  }

  @JsonValue
  public String getValue() {
    return value;
  }
}
