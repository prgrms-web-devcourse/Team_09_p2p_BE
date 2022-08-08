package com.prgrms.p2p.domain.course.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;


@Getter
@SuperBuilder
public class UpdateCoursePlaceRequest extends CreateCoursePlaceRequest {

  private Long coursePlaceId;
  private String imageUrl;
}
