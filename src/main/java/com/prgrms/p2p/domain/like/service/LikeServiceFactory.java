package com.prgrms.p2p.domain.like.service;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikeServiceFactory {

  private final Map<String, LikeService> likeServiceMap;

  public LikeService getLikeService(String name) {
    return likeServiceMap.get(name);
  }
}
