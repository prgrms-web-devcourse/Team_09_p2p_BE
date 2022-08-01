package com.prgrms.p2p.domain.place.dto;

import com.prgrms.p2p.domain.place.entity.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchPlaceRequest {

  private String keyword;
  private Category category;
}