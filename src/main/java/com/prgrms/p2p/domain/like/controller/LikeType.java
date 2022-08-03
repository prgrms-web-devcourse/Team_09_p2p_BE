package com.prgrms.p2p.domain.like.controller;

import com.prgrms.p2p.domain.like.service.LikeService;
import com.prgrms.p2p.domain.like.service.LikeServiceFactory;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

public enum LikeType {
  COURSES("courseLikeService") {
    @Override
    public void toggle(Long userId, Long courseId) {
      getLikeService().toggle(userId, courseId);
    }
  }, PLACES("placeLikeService") {
    @Override
    public void toggle(Long userId, Long placeId) {
      getLikeService().toggle(userId, placeId);
    }
  };

  private final String name;
  private LikeService likeService;

  LikeType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public LikeService getLikeService() {
    return likeService;
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
        COURSES.likeService = likeServiceFactory.getLikeService(COURSES.name);
        PLACES.likeService = likeServiceFactory.getLikeService(PLACES.name);
      } catch (Exception e) {
        throw new RuntimeException(e.getMessage());
      }
    }
  }
}
