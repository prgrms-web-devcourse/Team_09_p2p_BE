package com.prgrms.p2p.domain.like.service;

import com.prgrms.p2p.domain.common.exception.BadRequestException;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikeServiceFactory {

  private final Map<String, LikeService> likeServiceMap;

  public LikeService getLikeService(String name) {
    LikeService likeService = likeServiceMap.get(name);
    if (Objects.isNull(likeService)){
      throw new BadRequestException("잘못된 type 요청입니다.");
    }
    return likeService;
  }
}
