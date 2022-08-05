package com.prgrms.p2p.domain.comment.dto;

import com.prgrms.p2p.domain.comment.entity.Visibility;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseCommentResponse {

  private Long id;
  private String comment;
  private Long rootCommentId;
  private Long courseId;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Visibility visibility;

  private UserDto userDto;

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class UserDto {

    private Long id;
    private String NickName;
    private String profileImage;
  }
}