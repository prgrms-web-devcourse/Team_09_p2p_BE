package com.prgrms.p2p.domain.bookmark.service;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookmarkServiceFactory {
  private final Map<String, BookmarkService> bookmarkServiceMap;

  public BookmarkService getBookmarkService(String name){
    return bookmarkServiceMap.get(name);
  }
}
