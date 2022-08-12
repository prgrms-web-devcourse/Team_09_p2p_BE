package com.prgrms.p2p.domain.course.dto;

import com.prgrms.p2p.domain.place.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CoursePlaceResponse {

  private Long id;
  private Long placeId;
  private String kakaoMapId;
  private String name;
  private String description;
  private String address;
  private String latitude;
  private String longitude;
  private Category category;
  private String phoneNumber;
  private String imageUrl;
  private Boolean isRecommended;
  private Boolean isThumbnail;
}
