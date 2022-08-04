package com.prgrms.p2p.domain.bookmark.service;

import com.prgrms.p2p.domain.common.exception.BadRequestException;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookmarkServiceFactory {

  private final Map<String, BookmarkService> bookmarkServiceMap;

  public BookmarkService getBookmarkService(String name) {
    BookmarkService bookmarkService = bookmarkServiceMap.get(name);
    if (Objects.isNull(bookmarkService)) {
      throw new BadRequestException("잘못된 type 요청입니다.");
    }
    return bookmarkService;
  }
}
