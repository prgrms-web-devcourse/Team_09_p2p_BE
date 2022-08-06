package com.prgrms.p2p.domain.like.service;

public interface LikeService {

  void toggle(Long userId, Long targetId);

  Long countByUserId(Long userId);
}
