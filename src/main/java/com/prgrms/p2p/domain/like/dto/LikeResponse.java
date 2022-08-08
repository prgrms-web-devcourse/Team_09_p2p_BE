package com.prgrms.p2p.domain.like.dto;

import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
public class LikeResponse {
  private Long id;
  private Boolean isLiked;
}
