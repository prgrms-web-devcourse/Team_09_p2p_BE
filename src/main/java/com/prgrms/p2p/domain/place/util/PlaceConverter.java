package com.prgrms.p2p.domain.place.util;

import com.prgrms.p2p.domain.bookmark.entity.PlaceBookmark;
import com.prgrms.p2p.domain.like.entity.PlaceLike;
import com.prgrms.p2p.domain.place.dto.DetailPlaceResponse;
import com.prgrms.p2p.domain.place.dto.SummaryPlaceResponse;
import com.prgrms.p2p.domain.place.entity.Place;
import java.util.List;
import java.util.Optional;

public class PlaceConverter {

  public static DetailPlaceResponse toDetailPlaceResponse(Place place, Optional<Long> userId) {

    Integer likeCount = place.getPlaceLikes().size();
    Integer usedCount = place.getCoursePlaces().size();
    String imageUrl = getString(place, usedCount);

    Boolean liked = false;
    Boolean bookmarked = false;
    if (userId.isPresent()) {
      List<PlaceLike> placeLikes = place.getPlaceLikes();
      liked = placeLikes.stream().anyMatch(pl -> pl.getUserId().equals(userId));

      List<PlaceBookmark> placeBookmarks = place.getPlaceBookmarks();
      bookmarked = placeBookmarks.stream().anyMatch(pb -> pb.getUserId().equals(userId));
    }

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
        .build();
  }

  public static SummaryPlaceResponse toSummaryPlaceResponse(Place place, Optional<Long> userId) {

    Integer likeCount = place.getPlaceLikes().size();
    Integer usedCount = place.getCoursePlaces().size();
    String imageUrl = getString(place, usedCount);

    Boolean bookmarked = false;
    if (userId.isPresent()) {
      List<PlaceBookmark> placeBookmarks = place.getPlaceBookmarks();
      bookmarked = placeBookmarks.stream().anyMatch(pb -> pb.getUserId().equals(userId));
    }

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

  private static String getString(Place place, Integer usedCount) {
    return usedCount > 0 ? place.getCoursePlaces().get(0).getImageUrl() : null;
  }
}