package com.prgrms.p2p.domain.course.dto;

import com.prgrms.p2p.domain.course.entity.Period;
import com.prgrms.p2p.domain.course.entity.Region;
import com.prgrms.p2p.domain.course.entity.Spot;
import com.prgrms.p2p.domain.course.entity.Theme;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCourseRequest {

  private String title;
  @Builder.Default
  private Region region = Region.전체보기;
  private Period period;
  @Builder.Default
  private List<Theme> themes = new ArrayList<>();
  @Builder.Default
  private List<Spot> spots = new ArrayList<>();
  @Builder.Default
  private List<UpdateCoursePlaceRequest> places = new ArrayList<>();
}
