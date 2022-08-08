package com.prgrms.p2p.domain.like.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LikeResponse {
  private Long id;
  private Boolean isLiked;
}
