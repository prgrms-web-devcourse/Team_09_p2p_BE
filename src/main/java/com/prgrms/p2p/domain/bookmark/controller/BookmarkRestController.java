package com.prgrms.p2p.domain.bookmark.controller;

import com.prgrms.p2p.domain.bookmark.dto.BookmarkResponse;
import com.prgrms.p2p.domain.user.aop.annotation.Auth;
import com.prgrms.p2p.domain.user.aop.annotation.CurrentUser;
import com.prgrms.p2p.domain.user.dto.SignUpResponse;
import com.prgrms.p2p.domain.user.pojo.CustomUserDetails;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"Bookmark API"})
@RestController
@RequestMapping("/api/v1/bookmarks")
@RequiredArgsConstructor
public class BookmarkRestController {

  @Operation(summary = "북마크 토글 기능", description = "로그인 이후 원하는 장소와 코스에 북마트를 토글합니다.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "북마크 토글 성공", response = SignUpResponse.class),
      @ApiResponse(code = 401, message = "인증 받지 않은 사용자"),
      @ApiResponse(code = 404, message = "존재하지 않은 장소/코스로 요청")
  })
  @Auth
  @GetMapping("/{type}/{id}")
  public ResponseEntity<BookmarkResponse> toggle(
      @PathVariable("type") String type, @PathVariable("id") Long id,
      @CurrentUser CustomUserDetails user) {
    BookmarkResponse response = BookmarkType.of(type).toggle(user.getId(), id);
    return ResponseEntity.ok(response);
  }
}
