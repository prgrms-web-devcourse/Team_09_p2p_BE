package com.prgrms.p2p.domain.bookmark.controller;

import com.prgrms.p2p.domain.bookmark.dto.BookmarkResponse;
import com.prgrms.p2p.domain.bookmark.service.BookmarkService;
import com.prgrms.p2p.domain.bookmark.service.BookmarkServiceFactory;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

public enum BookmarkType {
  COURSES("courseBookmarkService") {
    @Override
    public BookmarkResponse toggle(Long userId, Long courseId) {
      return getBookmarkService().toggle(userId, courseId);
    }
  }, PLACES("placeBookmarkService") {
    @Override
    public BookmarkResponse toggle(Long userId, Long placeId) {
      return getBookmarkService().toggle(userId, placeId);
    }
  };

  private final String name;
  private BookmarkService bookmarkService;

  BookmarkType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public BookmarkService getBookmarkService() {
    return bookmarkService;
  }

  public abstract BookmarkResponse toggle(Long userId, Long targetId);

  public static BookmarkType of(String type) {
    return BookmarkType.valueOf(type.toUpperCase());
  }

  @Component
  @RequiredArgsConstructor
  public static class BookmarkServiceInjector {

    private final BookmarkServiceFactory bookmarkServiceFactory;

    @PostConstruct
    public void postConstruct() {
      try {
        COURSES.bookmarkService = bookmarkServiceFactory.getBookmarkService(COURSES.name);
        PLACES.bookmarkService = bookmarkServiceFactory.getBookmarkService(PLACES.name);
      } catch (Exception e) {
        throw new RuntimeException(e.getMessage());
      }
    }
  }
}
