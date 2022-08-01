package com.prgrms.p2p.domain.course.dto;

import com.prgrms.p2p.domain.course.entity.CoursePlace;
import com.prgrms.p2p.domain.course.entity.Period;
import com.prgrms.p2p.domain.course.entity.Region;
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
public class SummaryCourseResponse {

  private Long id;
  private String title;
  private String thumbnail;
  private Region region;
  private Period period;
  private List<Theme> themes = new ArrayList<>();
  private List<CoursePlace> places = new ArrayList<>();
  private Integer likes;
  private Boolean isBookmarked;
  private String nickname;
}
