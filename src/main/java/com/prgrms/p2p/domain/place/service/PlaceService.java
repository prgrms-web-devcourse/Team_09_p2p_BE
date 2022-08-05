package com.prgrms.p2p.domain.place.service;

import static com.prgrms.p2p.domain.place.util.PlaceConverter.toDetailPlaceResponse;
import static com.prgrms.p2p.domain.place.util.PlaceConverter.toSummaryPlaceResponse;

import com.prgrms.p2p.domain.common.exception.BadRequestException;
import com.prgrms.p2p.domain.course.dto.CreateCoursePlaceRequest;
import com.prgrms.p2p.domain.course.util.CoursePlaceConverter;
import com.prgrms.p2p.domain.place.dto.DetailPlaceResponse;
import com.prgrms.p2p.domain.place.dto.SearchPlaceRequest;
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
  public Place save(CreateCoursePlaceRequest createCoursePlaceRequest) {
    Place place = CoursePlaceConverter.toPlace(createCoursePlaceRequest);
    return placeRepository.save(place);
  }

  @Transactional
  public Optional<Place> findAndUpdateExistPlace(
      CreateCoursePlaceRequest createCoursePlaceRequest, String imageUrl) {
    Optional<Place> place =
        placeRepository.findByKakaoMapId(createCoursePlaceRequest.getKakaoMapId());
    place.ifPresent(p -> update(p, createCoursePlaceRequest));
    return place;
  }

  public DetailPlaceResponse findDetail(Long placeId, Optional<Long> userId) {
    Place place = placeRepository.findById(placeId).orElseThrow(RuntimeException::new);

    return toDetailPlaceResponse(place, userId);
  }

  public Slice<SummaryPlaceResponse> findSummaryList(
      SearchPlaceRequest searchPlaceReq, Pageable pageable, Long userId) {
    Slice<Place> placeList = placeRepository.searchPlace(searchPlaceReq, pageable);
    return placeList.map(p -> toSummaryPlaceResponse(p, Optional.ofNullable(userId)));
  }

  public Slice<SummaryPlaceResponse> findBookmarkedPlaceList(Long userId, Long targetUserId, Pageable pageable) {
    if (Objects.isNull(targetUserId)) {
      throw new BadRequestException("입력값을 확인해주세요.");
    }
    Slice<Place> bookmarkedPlace = placeRepository.findBookmarkedPlace(targetUserId, pageable);
    return bookmarkedPlace.map(place -> toSummaryPlaceResponse(place, Optional.ofNullable(userId)));
  }

  private void update(Place place, CreateCoursePlaceRequest updateReq) {
    place.changeName(place.getName());
    Address newAddress =
        new Address(updateReq.getAddressName(), updateReq.getRoadAddressName());
    place.changeAddress(newAddress);
    place.changeCategory(updateReq.getCategory());
    place.changePhoneNumber(updateReq.getPhoneNumber());
  }
}