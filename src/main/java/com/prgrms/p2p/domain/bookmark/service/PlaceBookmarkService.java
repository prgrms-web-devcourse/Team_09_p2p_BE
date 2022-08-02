package com.prgrms.p2p.domain.bookmark.service;

import com.prgrms.p2p.domain.bookmark.entity.PlaceBookmark;
import com.prgrms.p2p.domain.bookmark.repository.PlaceBookmarkRepository;
import com.prgrms.p2p.domain.place.entity.Place;
import com.prgrms.p2p.domain.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlaceBookmarkService implements BookmarkPolicy {

  private final PlaceRepository placeRepository;
  private final PlaceBookmarkRepository placeBookmarkRepository;

  @Override
  @Transactional
  public void toggle(Long userId, Long placeId) {
    Place place = placeRepository.findById(placeId)
        .orElseThrow(() -> new RuntimeException("존재하지 않는 장소에 좋아요를 시도했습니다."));
    placeBookmarkRepository.findByUserIdAndPlace(userId, place)
        .ifPresentOrElse(this::unbookmark, () -> bookmark(userId, place));
  }

  private void bookmark(Long userId, Place place) {
    PlaceBookmark placeBookmark = new PlaceBookmark(userId, place);
    placeBookmarkRepository.save(placeBookmark);
  }

  private void unbookmark(PlaceBookmark placeBookmark) {
    placeBookmark.deletePlace(placeBookmark.getPlace());
    placeBookmarkRepository.delete(placeBookmark);
  }
}
