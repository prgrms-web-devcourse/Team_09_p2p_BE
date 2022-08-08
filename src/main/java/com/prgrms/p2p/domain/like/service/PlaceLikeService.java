package com.prgrms.p2p.domain.like.service;

import com.prgrms.p2p.domain.common.exception.NotFoundException;
import com.prgrms.p2p.domain.like.dto.LikeResponse;
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
public class PlaceLikeService implements LikeService {

  private final PlaceRepository placeRepository;
  private final PlaceLikeRepository placeLikeRepository;

  @Override
  @Transactional
  public LikeResponse toggle(Long userId, Long placeId) {
    Place place = placeRepository.findById(placeId)
        .orElseThrow(() -> new NotFoundException("존재하지 않는 장소에 좋아요를 시도했습니다."));
    LikeResponse response = new LikeResponse();
    response.setId(placeId);
    placeLikeRepository.findByUserIdAndPlace(userId, place)
        .ifPresentOrElse(placeLike -> {
          dislike(placeLike);
          response.setIsLiked(Boolean.FALSE);
        }, () -> {
          like(userId, place);
          response.setIsLiked(Boolean.TRUE);
        });
    return response;
  }

  @Override
  public Long countByUserId(Long userId) {
    return placeLikeRepository.countByUserId(userId);
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
