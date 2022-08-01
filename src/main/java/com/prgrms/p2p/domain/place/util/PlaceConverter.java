package com.prgrms.p2p.domain.place.util;

import com.prgrms.p2p.domain.place.dto.DetailPlaceResponse;
import com.prgrms.p2p.domain.place.dto.SummaryPlaceResponse;
import com.prgrms.p2p.domain.place.entity.Place;

public class PlaceConverter {

  public static DetailPlaceResponse toDetailPlaceResponse(Place place) {

    Integer likeCount = place.getPlaceLikes().size();
    Integer usedCount = place.getCoursePlaces().size();
    String imageUrl = getString(place, usedCount);

    return DetailPlaceResponse.builder()
        .id(place.getId())
        .name(place.getName())
        .addressName(place.getAddress().getAddressName())
        .roadAddressName(place.getAddress().getRoadAddressName())
        .latitude(place.getLatitude())
        .longitude(place.getLongitude())
        .category(place.getCategory().toString())
        .phone(place.getPhoneNumber().getNumber())
        .imageUrl(imageUrl)
        .likeCount(likeCount)
        .usedCount(usedCount)
        .build();
  }

  public static SummaryPlaceResponse toSummaryPlaceResponse(Place place) {

    Integer likeCount = place.getPlaceLikes().size();
    Integer usedCount = place.getCoursePlaces().size();
    String imageUrl = getString(place, usedCount);

    return SummaryPlaceResponse.builder()
        .id(place.getId())
        .title(place.getName())
        .likeCount(likeCount)
        .usedCount(usedCount)
        .category(place.getCategory().toString())
        .thumbnail(imageUrl)
        .build();
  }

  private static String getString(Place place, Integer usedCount) {
    return usedCount > 0 ? place.getCoursePlaces().get(0).getImageUrl() : null;
  }
}