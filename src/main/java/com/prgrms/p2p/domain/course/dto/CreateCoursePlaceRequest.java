package com.prgrms.p2p.domain.course.dto;

import com.prgrms.p2p.domain.place.entity.Category;
import com.prgrms.p2p.domain.place.entity.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCoursePlaceRequest {

  private String kakaoMapId;
  private String name;
  private String addressName;
  private String roadAddressName;
  private String latitude;
  private String longitude;
  private Category category;
  private PhoneNumber phoneNumber;
  private Boolean isRecommended;
}
