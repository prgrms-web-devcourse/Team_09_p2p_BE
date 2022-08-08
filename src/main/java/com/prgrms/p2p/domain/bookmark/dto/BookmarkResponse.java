package com.prgrms.p2p.domain.bookmark.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
public class BookmarkResponse {
  private Long id;
  private Boolean isBookmarked;
}
