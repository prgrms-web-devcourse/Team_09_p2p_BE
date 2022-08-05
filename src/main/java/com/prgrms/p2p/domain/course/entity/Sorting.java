package com.prgrms.p2p.domain.course.entity;

import com.querydsl.core.types.OrderSpecifier;

public enum Sorting {
  최신순 {
    @Override
    public OrderSpecifier<?> expression() {
      return QCourse.course.createdAt.desc();
    }
  }, 인기순 {
    @Override
    public OrderSpecifier<?> expression() {
      return QCourse.course.courseLikes.size().desc();
    }
  };

  public abstract OrderSpecifier<?> expression();
}
