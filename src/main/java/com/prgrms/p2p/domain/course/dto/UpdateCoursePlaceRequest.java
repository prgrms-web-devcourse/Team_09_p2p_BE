package com.prgrms.p2p.domain.course.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;


@Getter
@SuperBuilder
@AllArgsConstructor
public class UpdateCoursePlaceRequest extends CreateCoursePlaceRequest {

  private Long coursePlaceId;
  private String imageUrl;
}
