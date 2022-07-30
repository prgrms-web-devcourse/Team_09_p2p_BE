package com.prgrms.p2p.domain.place.dto;

import com.prgrms.p2p.domain.place.entity.Category;
import com.prgrms.p2p.domain.place.entity.PhoneNumber;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatePlaceRequest {

  private String kakaoMapId;
  private String name;
  private String addressName;
  private String roadAddressName;
  private String latitude;
  private String longitude;
  private Category category;
  private PhoneNumber phoneNumber;
}