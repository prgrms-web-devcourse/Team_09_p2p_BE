package com.prgrms.p2p.domain.like.service;

import com.prgrms.p2p.domain.like.entity.PlaceLike;
import com.prgrms.p2p.domain.like.repository.PlaceLikeRepository;
import com.prgrms.p2p.domain.place.entity.Place;
import com.prgrms.p2p.domain.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlaceLikeService {

  private final PlaceLikeRepository placeLikeRepository;
  private final PlaceRepository placeRepository;

  @Transactional
  public void toggle(Long userId, Long placeId) {
    Place place = placeRepository.findById(placeId)
        .orElseThrow(() -> new RuntimeException("존재하지 않는 장소에 좋아요를 시도했습니다."));
    placeLikeRepository.findByUserIdAndPlace(userId, place)
        .ifPresentOrElse(this::dislike, () -> like(userId, place));
  }

  private void like(Long userId, Place place) {
    PlaceLike placeLike = new PlaceLike(userId, place);
    placeLikeRepository.save(placeLike);
  }

  private void dislike(PlaceLike placeLike) {
    placeLike.deletePlace(placeLike.getPlace());
    placeLikeRepository.delete(placeLike);
  }
}
