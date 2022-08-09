package com.prgrms.p2p.domain.place.dto;

import com.prgrms.p2p.domain.course.entity.Region;
import com.prgrms.p2p.domain.course.entity.Sorting;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchPlaceDto {

  private Optional<String> keyword;
  private Optional<Region> region;
  private Optional<Sorting> sorting;
}
