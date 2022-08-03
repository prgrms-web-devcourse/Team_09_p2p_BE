package com.prgrms.p2p.domain.place.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchPlaceRequest {

  private String keyword;
  private Long userId;
}