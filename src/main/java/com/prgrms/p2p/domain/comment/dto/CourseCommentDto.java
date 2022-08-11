package com.prgrms.p2p.domain.comment.dto;

import io.swagger.annotations.ApiParam;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseCommentDto {

  @ApiParam(value = "댓글 ID", example = "21")
  private Long id;

  @ApiParam(value = "댓글 내용", example = "게시글 잘 봤습니다.")
  private String comment;

  @ApiParam(value = "부모 댓글 ID", example = "19")
  private Long rootCommentId;

  @ApiParam(value = "댓글 작성 시간")
  private LocalDateTime createdAt;

  @ApiParam(value = "댓글 최근 수정 시간")
  private LocalDateTime updatedAt;

  @ApiParam(value = "댓글 작성자 정보")
  private UserDto user;

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class UserDto {

    @ApiParam(value = "댓글 작성자 ID", example = "3")
    private Long id;

    @ApiParam(value = "댓글 작성자 닉네임", example = "코범석")
    private String nickname;

    @ApiParam(value = "댓글 작성자 프로필 이미지")
    private String profileImage;
  }
}