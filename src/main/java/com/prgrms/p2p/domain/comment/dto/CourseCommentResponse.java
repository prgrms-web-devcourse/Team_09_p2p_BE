package com.prgrms.p2p.domain.comment.dto;

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

  private Long id; //courseId
  private Long totalCount;
  private List<CourseCommentDto> courseComments;
}
