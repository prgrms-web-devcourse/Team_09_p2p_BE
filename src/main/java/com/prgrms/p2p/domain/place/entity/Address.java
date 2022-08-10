package com.prgrms.p2p.domain.place.entity;

import com.prgrms.p2p.domain.common.exception.BadRequestException;
import java.util.Objects;
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
    if (Objects.isNull(addressName) && Objects.isNull(roadAddressName)) {
      throw new BadRequestException("2가지 주소 중 최소한 하나는 있어야 함");
    }
    this.addressName = addressName;
    this.roadAddressName = roadAddressName;
  }
}
