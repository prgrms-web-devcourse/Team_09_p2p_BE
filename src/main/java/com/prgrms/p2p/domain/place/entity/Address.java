package com.prgrms.p2p.domain.place.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

// TODO: 2022/07/27 kakao에서 넘어오는 형식 맞춰서 수정 필요
@Embeddable
@Getter
@Access(value = AccessType.FIELD)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Address {

  private String addressName;
  private String roadAddressName;

  public Address(String addressName, String roadAddressName) {
    this.addressName = addressName;
    this.roadAddressName = roadAddressName;
  }
}
