package com.prgrms.p2p.domain.place.util;

import com.prgrms.p2p.domain.bookmark.entity.PlaceBookmark;
import com.prgrms.p2p.domain.course.entity.Region;
import com.prgrms.p2p.domain.course.entity.Sorting;
import com.prgrms.p2p.domain.like.entity.PlaceLike;
import com.prgrms.p2p.domain.place.dto.DetailPlaceResponse;
import com.prgrms.p2p.domain.place.dto.SearchPlaceDto;
import com.prgrms.p2p.domain.place.dto.SummaryPlaceResponse;
import com.prgrms.p2p.domain.place.entity.Place;
import java.util.List;
import java.util.Optional;

public class PlaceConverter {

  public static DetailPlaceResponse toDetailPlaceResponse(Place place, Optional<Long> userId, Integer commentCount) {

    Integer likeCount = place.getPlaceLikes().size();
    Integer usedCount = place.getCoursePlaces().size();
    String imageUrl = getString(place, usedCount);

    Boolean liked = getLiked(place, userId);
    Boolean bookmarked = getaBoolean(place, userId);

    return DetailPlaceResponse.builder()
        .id(place.getId())
        .name(place.getName())
        .addressName(place.getAddress().getAddressName())
        .roadAddressName(place.getAddress().getRoadAddressName())
        .latitude(place.getLatitude())
        .longitude(place.getLongitude())
        .category(place.getCategory())
        .phoneNumber(place.getPhoneNumber())
        .imageUrl(imageUrl)
        .liked(liked)
        .bookmarked(bookmarked)
        .likeCount(likeCount)
        .usedCount(usedCount)
        .kakaoMapId(place.getKakaoMapId())
        .comments(commentCount)
        .build();
  }

  public static SummaryPlaceResponse toSummaryPlaceResponse(Place place, Optional<Long> userId) {

    Integer likeCount = place.getPlaceLikes().size();
    Integer usedCount = place.getCoursePlaces().size();
    String imageUrl = getString(place, usedCount);

    Boolean bookmarked = getaBoolean(place, userId);

    return SummaryPlaceResponse.builder()
        .id(place.getId())
        .title(place.getName())
        .likeCount(likeCount)
        .usedCount(usedCount)
        .category(place.getCategory())
        .thumbnail(imageUrl)
        .bookmarked(bookmarked)
        .build();
  }

  public static SearchPlaceDto toSearchPlaceDto(
      Optional<String> keyword,
      Optional<Region> region,
      Optional<Sorting> sorting) {
    return SearchPlaceDto.builder()
        .keyword(keyword)
        .region(region)
        .sorting(sorting)
        .build();
  }

  private static String getString(Place place, Integer usedCount) {
    return usedCount > 0 ? place.getCoursePlaces().get(0).getImageUrl() : null;
  }

  private static Boolean getLiked(Place place, Optional<Long> userId) {
    List<PlaceLike> placeLikes = place.getPlaceLikes();
    return userId.isPresent() && placeLikes.stream()
        .anyMatch(pl -> pl.getUserId().equals(userId.get()));
  }

  private static Boolean getaBoolean(Place place, Optional<Long> userId) {
    List<PlaceBookmark> placeBookmarks = place.getPlaceBookmarks();
    return userId.isPresent() && placeBookmarks.stream()
        .anyMatch(pb -> pb.getUserId().equals(userId.get()));
  }
}