package com.prgrms.p2p.domain.comment.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceCommentDto {

  private Long id;
  private String comment;
  private Long rootCommentId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private UserDto user;

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class UserDto {

    private Long id;
    private String nickname;
    private String profileImage;
  }
}