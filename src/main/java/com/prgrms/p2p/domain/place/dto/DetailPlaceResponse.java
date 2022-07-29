package com.prgrms.p2p.domain.place.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailPlaceResponse {

  private Long id;
  private String name;
  private String addressName;
  private String roadAddressName;
  private String latitude;
  private String longitude;
  private String category;
  private String phone;
  private String imageUrl;

  private Long likeCount;
  private Long usedCount;

  // TODO: 2022/07/29 사용된 코스 추천 몇가지 들어가야 함
}