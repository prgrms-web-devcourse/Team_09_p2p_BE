package com.prgrms.p2p.domain.like.controller;

import com.prgrms.p2p.domain.like.service.LikePolicy;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

public enum LikeType {
  COURSE("courseLikeService") {
    @Override
    public void toggle(Long userId, Long courseId) {
      getLikePolicy().toggle(userId, courseId);
    }
  }, PLACE("placeLikeService") {
    @Override
    public void toggle(Long userId, Long placeId) {
      getLikePolicy().toggle(userId, placeId);
    }
  };

  private final String name;
  private LikePolicy likePolicy;

  LikeType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public LikePolicy getLikePolicy() {
    return likePolicy;
  }

  public abstract void toggle(Long userId, Long targetId);

  public static LikeType of(String type) {
    return LikeType.valueOf(type.toUpperCase());
  }

  @Component
  @RequiredArgsConstructor
  public static class FruitEnumInjector {

    private final LikeServiceFactory likeServiceFactory;

    @PostConstruct
    public void postConstruct() {
      try {
        COURSE.likePolicy = likeServiceFactory.getLikeService(COURSE.name);
        PLACE.likePolicy = likeServiceFactory.getLikeService(PLACE.name);
      } catch (Exception e) {
        throw e;
      }
    }
  }
}
