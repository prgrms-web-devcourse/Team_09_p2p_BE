package com.prgrms.p2p.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Sex {
  MALE("male"),
  FEMALE("female");

  private final String value;

  Sex(String value) {
    this.value = value;
  }

  @JsonCreator
  public static Sex from(String value) {
    for (Sex status : Sex.values()) {
      if (status.getValue().equals(value)) {
        return status;
      }
    }
    return null;
  }

  @JsonValue
  public String getValue() {
    return value;
  }
}
