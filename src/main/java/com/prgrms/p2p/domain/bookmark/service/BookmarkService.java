package com.prgrms.p2p.domain.bookmark.service;

public interface BookmarkService {

  void toggle(Long userId, Long targetId);

  Long countByUserId(Long userId);
}
