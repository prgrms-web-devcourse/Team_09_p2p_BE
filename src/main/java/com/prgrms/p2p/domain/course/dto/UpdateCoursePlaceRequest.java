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
public class UpdateCoursePlaceRequest {

  private String kakaoMapId;
  private String name;
  private String description;
  private String addressName;
  private String roadAddressName;
  private String latitude;
  private String longitude;
  @Builder.Default
  private Category category = Category.DE9;
  private PhoneNumber phoneNumber;
  @Builder.Default
  private Boolean isRecommended = false;
  @Builder.Default
  private Boolean isThumbnail = false;
  private String imageUrl;
}
