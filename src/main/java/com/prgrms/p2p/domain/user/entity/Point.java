package com.prgrms.p2p.domain.user.entity;

public enum Point {
  MAKE_COURSE(50),
  MAKE_COMMENT(10),
  DELETE_COURSE(-50),
  DELETE_COMMENT(-10);

  private int score;

  Point(int score) {
    this.score = score;
  }

  public int getScore(){
    return score;
  }
}
