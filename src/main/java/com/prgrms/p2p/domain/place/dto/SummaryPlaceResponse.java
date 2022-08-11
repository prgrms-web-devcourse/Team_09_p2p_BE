package com.prgrms.p2p.domain.place.dto;

import com.prgrms.p2p.domain.place.entity.Category;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SummaryPlaceResponse {

  @ApiParam(value = "장소 ID", example = "3")
  private Long id;

  @ApiParam(value = "장소 이름", example = "서울역")
  private String title;

  @ApiParam(value = "받은 좋아요 개수", example = "1")
  private Integer likeCount;

  @ApiParam(value = "코스 게시글 작성에 사용된 빈도", example = "1")
  private Integer usedCount;

  @ApiParam(value = "장소 분류", example = "MT1")
  private Category category;

  @ApiParam(value = "장소 대표 이미지")
  private String thumbnail;

  @ApiParam(value = "유저의 북마크 여부", example = "true")
  private Boolean bookmarked;
}