package com.prgrms.p2p.domain.place.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class CreatePlaceRequest {

  private String kakaoMapId;
  private String name;
  private String addressName;
  private String roadAddressName;
  private String latitude;
  private String longitude;
  private String category;
  private String phoneNumber;
}