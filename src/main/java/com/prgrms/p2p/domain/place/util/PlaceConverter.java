package com.prgrms.p2p.domain.place.util;

import com.prgrms.p2p.domain.place.dto.CreatePlaceRequest;
import com.prgrms.p2p.domain.place.dto.DetailPlaceResponse;
import com.prgrms.p2p.domain.place.dto.SummaryPlaceResponse;
import com.prgrms.p2p.domain.place.entity.Address;
import com.prgrms.p2p.domain.place.entity.Place;

public class PlaceConverter {

  public static Place toPlace(CreatePlaceRequest createPlaceRequest) {
    return new Place(createPlaceRequest.getKakaoMapId(),
        createPlaceRequest.getName(),
        new Address(createPlaceRequest.getAddressName(), createPlaceRequest.getRoadAddressName()),
        createPlaceRequest.getLatitude(),
        createPlaceRequest.getLongitude(),
        createPlaceRequest.getCategory(),
        createPlaceRequest.getPhoneNumber(),
        null);
  }

  public static DetailPlaceResponse toDetailPlaceResponse(
      Place place, String imageUrl, Long likeCount, Long usedCount) {

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

  public static SummaryPlaceResponse toSummaryPlaceResponse(
      Place place, String imageUrl, Long likeCount, Long usedCount) {

    return SummaryPlaceResponse.builder()
        .id(place.getId())
        .title(place.getName())
        .likeCount(likeCount)
        .usedCount(usedCount)
        .category(place.getCategory().toString())
        .thumbnail(imageUrl)
        .build();
  }
}