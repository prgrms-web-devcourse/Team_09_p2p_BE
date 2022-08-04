package com.prgrms.p2p.domain.course.dto;

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
  private Integer seq;
  private String description;
  private String imageUrl;
  private Boolean recommended;
}
