package com.prgrms.p2p.domain.bookmark.controller;

import com.prgrms.p2p.domain.bookmark.dto.BookmarkResponse;
import com.prgrms.p2p.domain.user.aop.annotation.Auth;
import com.prgrms.p2p.domain.user.aop.annotation.CurrentUser;
import com.prgrms.p2p.domain.user.pojo.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bookmarks")
@RequiredArgsConstructor
public class BookmarkRestController {

  @Auth
  @GetMapping("/{type}/{id}")
  public ResponseEntity<BookmarkResponse> toggle(
      @PathVariable("type") String type, @PathVariable("id") Long id,
      @CurrentUser CustomUserDetails user) {
    BookmarkResponse response = BookmarkType.of(type).toggle(user.getId(), id);
    return ResponseEntity.ok(response);
  }
}
