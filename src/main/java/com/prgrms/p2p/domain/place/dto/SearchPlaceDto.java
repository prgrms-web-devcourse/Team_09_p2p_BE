package com.prgrms.p2p.domain.place.dto;

import com.prgrms.p2p.domain.course.entity.Region;
import com.prgrms.p2p.domain.course.entity.Sorting;
import io.swagger.annotations.ApiParam;
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

  @ApiParam(value = "장소 검색어", example = "횟집")
  private Optional<String> keyword;

  @ApiParam(value = "지역 검색 조건", example = "인천")
  private Optional<Region> region;

  @ApiParam(value = "장소 검색 정렬 순서", example = "최신순")
  private Optional<Sorting> sorting;
}
