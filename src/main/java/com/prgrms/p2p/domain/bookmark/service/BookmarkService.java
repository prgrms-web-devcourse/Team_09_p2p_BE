package com.prgrms.p2p.domain.bookmark.service;

import com.prgrms.p2p.domain.bookmark.dto.BookmarkResponse;

public interface BookmarkService {

  BookmarkResponse toggle(Long userId, Long targetId);

  Long countByUserId(Long userId);
}
