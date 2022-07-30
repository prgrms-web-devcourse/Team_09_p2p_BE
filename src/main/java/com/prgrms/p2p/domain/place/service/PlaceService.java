package com.prgrms.p2p.domain.place.service;

import static com.prgrms.p2p.domain.place.util.PlaceConverter.toDetailPlaceResponse;
import static com.prgrms.p2p.domain.place.util.PlaceConverter.toPlace;
import static com.prgrms.p2p.domain.place.util.PlaceConverter.toSummaryPlaceResponse;

import com.prgrms.p2p.domain.common.service.UploadService;
import com.prgrms.p2p.domain.place.dto.CreatePlaceRequest;
import com.prgrms.p2p.domain.place.dto.DetailPlaceResponse;
import com.prgrms.p2p.domain.place.dto.SearchPlaceRequest;
import com.prgrms.p2p.domain.place.dto.SummaryPlaceResponse;
import com.prgrms.p2p.domain.place.entity.Place;
import com.prgrms.p2p.domain.place.repository.PlaceRepository;
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
  private final UploadService uploadService;

  @Transactional
  public Long save(CreatePlaceRequest createPlaceRequest) {
    Place place = toPlace(createPlaceRequest);
    return placeRepository.save(place).getId();
  }

  public DetailPlaceResponse findDetail(Long placeId) {
    Place place = placeRepository.findById(placeId).orElseThrow(RuntimeException::new);
    String imageUrl = placeRepository.findFirstImage(placeId);
    Integer likeCount = place.getPlaceLikes().size();
    Integer usedCount = place.getCoursePlaces().size();

    return toDetailPlaceResponse(place, imageUrl, likeCount, usedCount);
  }

  public Slice<SummaryPlaceResponse> findSummaryList(
      SearchPlaceRequest searchPlaceRequest, Pageable pageable) {
    Slice<Place> placeList = placeRepository.searchPlace(searchPlaceRequest, pageable);
    return placeList.map(place -> toSummaryPlaceResponse(
        place,
        placeRepository.findFirstImage(place.getId()),
        place.getPlaceLikes().size(),
        place.getCoursePlaces().size()
    ));
  }
}