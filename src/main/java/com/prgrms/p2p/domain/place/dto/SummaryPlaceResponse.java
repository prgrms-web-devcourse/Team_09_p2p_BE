package com.prgrms.p2p.domain.place.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SummaryPlaceResponse {

  private Long id;
  private String title;
  private Integer likeCount;
  private Integer usedCount;
  private String category;
  private String thumbnail;
}