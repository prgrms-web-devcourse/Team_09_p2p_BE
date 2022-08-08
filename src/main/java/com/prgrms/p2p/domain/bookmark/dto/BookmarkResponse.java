package com.prgrms.p2p.domain.bookmark.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BookmarkResponse {
  private Long id;
  private Boolean isBookmarked;
}
