package com.prgrms.p2p.domain.course.dto;

import com.prgrms.p2p.domain.course.entity.Region;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchCourseRequest {
  private String keyword;
  private Region region;
  private String category;
  private String theme;
}
