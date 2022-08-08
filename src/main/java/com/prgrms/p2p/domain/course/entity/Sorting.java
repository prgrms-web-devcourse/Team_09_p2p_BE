package com.prgrms.p2p.domain.course.entity;

import com.prgrms.p2p.domain.place.entity.QPlace;
import com.querydsl.core.types.OrderSpecifier;

public enum Sorting {
  최신순 {
    @Override
    public OrderSpecifier<?> expression() {
      return QCourse.course.createdAt.desc();
    }

    @Override
    public OrderSpecifier<?> expressionForPlace() {
      return QPlace.place.createdAt.desc();
    }
  }, 인기순 {
    @Override
    public OrderSpecifier<?> expression() {
      return QCourse.course.courseLikes.size().desc();
    }

    @Override
    public OrderSpecifier<?> expressionForPlace() {
      return QPlace.place.placeLikes.size().desc();
    }
  };

  public abstract OrderSpecifier<?> expression();

  public abstract OrderSpecifier<?> expressionForPlace();
}
