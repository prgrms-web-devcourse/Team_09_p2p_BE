package com.prgrms.p2p.domain.bookmark.service;

public interface BookmarkPolicy {

  void toggle(Long userId, Long targetId);
}
