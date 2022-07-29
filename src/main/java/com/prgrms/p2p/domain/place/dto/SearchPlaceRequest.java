package com.prgrms.p2p.domain.place.dto;

import lombok.Getter;

@Getter
public class SearchPlaceRequest {

  private String keyword;
  private String category;
}