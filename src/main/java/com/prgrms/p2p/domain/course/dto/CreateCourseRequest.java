package com.prgrms.p2p.domain.course.dto;

import com.prgrms.p2p.domain.course.entity.Period;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCourseRequest {

  private String title;
  private String region;
  private Period period;
  private String description;
  private List<String> themes;
  private List<CreateCoursePlaceRequest> places;
}
