package com.prgrms.p2p.domain.place.entity;

public enum Category {
  MOUNTAIN("산"),
  SEA("바다"),
  CAFE("카페")
  ;

  private String category;

  Category(String category) {
    this.category = category;
  }
}
