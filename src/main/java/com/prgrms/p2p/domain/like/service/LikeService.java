package com.prgrms.p2p.domain.like.service;

import com.prgrms.p2p.domain.like.dto.LikeResponse;

public interface LikeService {

  LikeResponse toggle(Long userId, Long targetId);

  Long countByUserId(Long userId);
}
