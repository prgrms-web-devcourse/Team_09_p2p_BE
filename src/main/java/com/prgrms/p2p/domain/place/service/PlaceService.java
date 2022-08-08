package com.prgrms.p2p.domain.place.service;

import static com.prgrms.p2p.domain.course.util.CoursePlaceConverter.*;
import static com.prgrms.p2p.domain.place.util.PlaceConverter.toDetailPlaceResponse;
import static com.prgrms.p2p.domain.place.util.PlaceConverter.toSummaryPlaceResponse;

import com.prgrms.p2p.domain.common.exception.BadRequestException;
import com.prgrms.p2p.domain.course.dto.CreateCoursePlaceRequest;
import com.prgrms.p2p.domain.place.dto.DetailPlaceResponse;
import com.prgrms.p2p.domain.place.dto.SummaryPlaceResponse;
import com.prgrms.p2p.domain.place.entity.Address;
import com.prgrms.p2p.domain.place.entity.Place;
import com.prgrms.p2p.domain.place.repository.PlaceRepository;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlaceService {

  private final PlaceRepository placeRepository;

  @Transactional
  public Place save(CreateCoursePlaceRequest createPlaceReq) {
    Optional<Place> placeByKakaoMapId =
        placeRepository.findByKakaoMapId(createPlaceReq.getKakaoMapId());

    Place place = placeByKakaoMapId.map(p -> {
              update(p, createPlaceReq);
              return p;
            }
        ).orElseGet(() -> placeRepository.save(toPlace(createPlaceReq)));
    return place;
  }

  @Transactional
  public Optional<Place> findAndUpdateExistPlace(
      CreateCoursePlaceRequest createCoursePlaceRequest) {
    Optional<Place> place = placeRepository.findByKakaoMapId(
        createCoursePlaceRequest.getKakaoMapId());
    place.ifPresent(p -> update(p, createCoursePlaceRequest));
    return place;
  }

  public DetailPlaceResponse findDetail(Long placeId, Optional<Long> userId) {
    Place place = placeRepository.findById(placeId).orElseThrow(RuntimeException::new);

    return toDetailPlaceResponse(place, userId);
  }

  public Slice<SummaryPlaceResponse> findSummaryList(Optional<String> keyword, Pageable pageable,
      Long userId) {
    String keywords = keyword.isEmpty() ? "" : keyword.get();
    Slice<Place> placeList = placeRepository.searchPlace(keywords, pageable);
    return placeList.map(p -> toSummaryPlaceResponse(p, Optional.ofNullable(userId)));
  }

  public Slice<SummaryPlaceResponse> findBookmarkedPlaceList(Long userId, Long targetUserId,
      Pageable pageable) {
    if (Objects.isNull(targetUserId)) {
      throw new BadRequestException("입력값을 확인해주세요.");
    }
    Slice<Place> bookmarkedPlace = placeRepository.findBookmarkedPlace(targetUserId, pageable);
    return bookmarkedPlace.map(place -> toSummaryPlaceResponse(place, Optional.ofNullable(userId)));
  }

  private void update(Place place, CreateCoursePlaceRequest updateReq) {
    place.changeName(place.getName());
    Address newAddress = new Address(updateReq.getAddressName(), updateReq.getRoadAddressName());
    place.changeAddress(newAddress);
    place.changeCategory(updateReq.getCategory());
    place.changePhoneNumber(updateReq.getPhoneNumber());
  }
}