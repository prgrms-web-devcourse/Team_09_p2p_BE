package com.prgrms.p2p.domain.place.service;

import static com.prgrms.p2p.domain.place.util.PlaceConverter.toDetailPlaceResponse;
import static com.prgrms.p2p.domain.place.util.PlaceConverter.toPlace;

import com.prgrms.p2p.domain.common.service.UploadService;
import com.prgrms.p2p.domain.course.repository.CoursePlaceRepository;
import com.prgrms.p2p.domain.place.dto.CreatePlaceRequest;
import com.prgrms.p2p.domain.place.dto.DetailPlaceResponse;
import com.prgrms.p2p.domain.place.entity.Place;
import com.prgrms.p2p.domain.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlaceService {

  private final PlaceRepository placeRepository;
  private final UploadService uploadService;
  private final CoursePlaceRepository coursePlaceRepository;

  @Transactional
  public Long save(CreatePlaceRequest createPlaceRequest) {
    Place place = toPlace(createPlaceRequest);
    return placeRepository.save(place).getId();
  }

  public DetailPlaceResponse findDetail(Long placeId) {
    Place place = placeRepository.findById(placeId).orElseThrow(RuntimeException::new);
    String imageUrl = coursePlaceRepository.findFirstByPlace(place)
        .orElseThrow(RuntimeException::new).getImageUrl();
    Long likeCount = place.getPlaceLikes().stream().count();
    Long usedCount = place.getCoursePlaces().stream().count();

    return toDetailPlaceResponse(place, imageUrl, likeCount, usedCount);
  }
}