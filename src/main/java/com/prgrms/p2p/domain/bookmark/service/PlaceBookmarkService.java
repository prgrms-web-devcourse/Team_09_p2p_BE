package com.prgrms.p2p.domain.bookmark.service;

import com.prgrms.p2p.domain.bookmark.dto.BookmarkResponse;
import com.prgrms.p2p.domain.bookmark.entity.PlaceBookmark;
import com.prgrms.p2p.domain.common.exception.NotFoundException;
import com.prgrms.p2p.domain.bookmark.repository.PlaceBookmarkRepository;
import com.prgrms.p2p.domain.place.entity.Place;
import com.prgrms.p2p.domain.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlaceBookmarkService implements BookmarkService {

  private final PlaceRepository placeRepository;
  private final PlaceBookmarkRepository placeBookmarkRepository;

  @Override
  @Transactional
  public BookmarkResponse toggle(Long userId, Long placeId) {
    Place place = placeRepository.findById(placeId)
        .orElseThrow(() -> new NotFoundException("존재하지 않는 장소에 북마크를 시도했습니다."));
    BookmarkResponse response = new BookmarkResponse();
    response.setId(placeId);
    placeBookmarkRepository.findByUserIdAndPlace(userId, place)
        .ifPresentOrElse(placeBookmark -> {
          unbookmark(placeBookmark);
          response.setIsBookmarked(Boolean.FALSE);
        }, () -> {
          bookmark(userId, place);
          response.setIsBookmarked(Boolean.TRUE);
        });
    return response;
  }

  @Override
  public Long countByUserId(Long userId) {
    return placeBookmarkRepository.countByUserId(userId);
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
