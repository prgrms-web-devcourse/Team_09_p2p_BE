package com.prgrms.p2p.domain.user.entity;

public enum Grade {
  BRONZE(100),
  SLIVER(300),
  GOLD(500),
  PLATINUM(1000),
  DIAMOND(3000);

  private int score;

  Grade(int score) {
    this.score = score;
  }

  public int getScore(){
    return score;
  }
}
