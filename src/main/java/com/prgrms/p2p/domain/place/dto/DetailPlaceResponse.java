package com.prgrms.p2p.domain.place.dto;

import com.prgrms.p2p.domain.place.entity.Category;
import com.prgrms.p2p.domain.place.entity.PhoneNumber;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailPlaceResponse {

  @ApiParam(value = "장소 ID", example = "3")
  private Long id;

  @ApiParam(value = "장소 이름", example = "서울역")
  private String name;

  @ApiParam(value = "지번 주소", example = "서울 광진구 중곡동 157-1")
  private String addressName;

  @ApiParam(value = "도로명 주소", example = "서울 광진구 능동로 314")
  private String roadAddressName;

  @ApiParam(value = "위도", example = "127.08008828553022")
  private String latitude;

  @ApiParam(value = "경도", example = "27.08008828553022")
  private String longitude;

  @ApiParam(value = "장소 분류", example = "MT1")
  private Category category;

  @ApiParam(value = "전화번호", example = "02-123-4567")
  private PhoneNumber phoneNumber;

  @ApiParam(value = "장소 이미지")
  private String imageUrl;

  @ApiParam(value = "유저의 좋아요 여부", example = "true")
  private Boolean liked;

  @ApiParam(value = "유저의 북마크 여부", example = "true")
  private Boolean bookmarked;

  @ApiParam(value = "받은 좋아요 개수", example = "1")
  private Integer likeCount;

  @ApiParam(value = "코스 게시글 작성에 사용된 빈도", example = "1")
  private Integer usedCount;

  @ApiParam(value = "카카오 맵 아이디", example = "11231422")
  private String kakaoMapId;

  @ApiParam(value = "댓글 개수", example = "0")
  private Long comments;
}