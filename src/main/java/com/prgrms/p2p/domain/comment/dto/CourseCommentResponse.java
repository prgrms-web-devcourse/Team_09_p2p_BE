package com.prgrms.p2p.domain.comment.dto;

import io.swagger.annotations.ApiParam;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseCommentResponse {

  @ApiParam(value = "코스(장소) ID")
  private Long id; //courseId or placeId

  @ApiParam(value = "코스(장소)에 작성된 댓글 총 개수")
  private Long totalCount;

  @ApiParam(value = "댓글 정보 리스트")
  private List<CourseCommentDto> courseComments;
}
