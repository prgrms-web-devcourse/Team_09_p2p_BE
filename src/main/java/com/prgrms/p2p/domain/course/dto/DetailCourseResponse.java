package com.prgrms.p2p.domain.course.dto;

import com.prgrms.p2p.domain.course.entity.Period;
import com.prgrms.p2p.domain.course.entity.Region;
import com.prgrms.p2p.domain.course.entity.Spot;
import com.prgrms.p2p.domain.course.entity.Theme;
import java.time.LocalDateTime;
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
public class DetailCourseResponse {

  private Long id;
  private String title;
  private String thumbnail;
  private Period period;
  private Region region;
  @Builder.Default
  private List<Theme> themes = new ArrayList<>();
  @Builder.Default
  private List<Spot> spots = new ArrayList<>();
  @Builder.Default
  private List<CoursePlaceResponse> places = new ArrayList<>();
  private Integer likes;
  private Long comments;
  private Boolean isLiked;
  private Boolean isBookmarked;
  private Long userId;
  private String nickname;
  private String profileImage;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
