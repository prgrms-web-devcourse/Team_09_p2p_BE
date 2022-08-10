package com.prgrms.p2p.domain.place.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
