package com.prgrms.p2p.domain.like.service;

public interface LikePolicy {

  void toggle(Long userId, Long targetId);
}
