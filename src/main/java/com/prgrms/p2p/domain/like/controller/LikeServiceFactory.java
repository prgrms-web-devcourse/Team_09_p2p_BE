package com.prgrms.p2p.domain.like.controller;

import com.prgrms.p2p.domain.like.service.LikePolicy;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikeServiceFactory {

  private final Map<String, LikePolicy> likePolicyMap;

  public LikePolicy getLikeService(String name) {
    return likePolicyMap.get(name);
  }
}
