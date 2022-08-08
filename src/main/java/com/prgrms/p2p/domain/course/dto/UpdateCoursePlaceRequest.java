package com.prgrms.p2p.domain.course.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCoursePlaceRequest extends CoursePlaceRequest {

  private Long coursePlaceId;
  private String imageUrl;
}
