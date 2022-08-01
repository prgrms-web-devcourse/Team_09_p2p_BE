package com.prgrms.p2p.domain.course.dto;

import com.prgrms.p2p.domain.course.entity.Region;

public class SearchCourseRequest {
  private String keyword;
  private Region region;
  private String place;
  private String theme;
}
