package com.prgrms.p2p.domain.course.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.Arrays;

public enum Region {
  서울, 인천, 대구, 대전, 광주, 부산, 울산, 경기, 세종, 강원, 충북, 충남, 경북, 경남, 전북, 전남, 제주;

  @JsonCreator
  public static Region of(String value) {
    return Arrays.stream(Region.values()).filter(region -> region.name().equalsIgnoreCase(value))
        .findFirst().orElse(null);
  }
}
