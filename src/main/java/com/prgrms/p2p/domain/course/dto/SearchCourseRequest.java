package com.prgrms.p2p.domain.course.dto;

import com.prgrms.p2p.domain.course.entity.Period;
import com.prgrms.p2p.domain.course.entity.Region;
import com.prgrms.p2p.domain.course.entity.Sorting;
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
public class SearchCourseRequest {

  private String keyword;
  private Region region;
  private Period period;
  private Long placeId;
  @Builder.Default
  private List<Spot> spots = new ArrayList<>();
  @Builder.Default
  private List<Theme> themes = new ArrayList<>();
  private Sorting sorting;
}
