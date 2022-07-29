package com.prgrms.p2p.domain.place.util;

import com.prgrms.p2p.domain.place.dto.CreatePlaceRequest;
import com.prgrms.p2p.domain.place.entity.Address;
import com.prgrms.p2p.domain.place.entity.Place;

public class PlaceConverter {

  public static Place toPlace(CreatePlaceRequest createPlaceRequest) {
    return new Place(createPlaceRequest.getKakaoMapId(),
        createPlaceRequest.getName(),
        new Address(createPlaceRequest.getAddress1(), createPlaceRequest.getAddress2()),
        createPlaceRequest.getLatitude(),
        createPlaceRequest.getLongitude(),
        createPlaceRequest.getCategory(),
        createPlaceRequest.getPhoneNumber(),
        null);
  }
}