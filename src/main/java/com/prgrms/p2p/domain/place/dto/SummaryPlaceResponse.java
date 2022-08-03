package com.prgrms.p2p.domain.place.dto;

import com.prgrms.p2p.domain.place.entity.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SummaryPlaceResponse {

  private Long id;
  private String title;
  private Integer likeCount;
  private Integer usedCount;
  private Category category;
  private String thumbnail;
  private Boolean bookmarked;
}