package com.prgrms.p2p.domain.bookmark.repository;

import com.prgrms.p2p.domain.bookmark.entity.PlaceBookmark;
import com.prgrms.p2p.domain.place.entity.Place;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceBookmarkRepository extends JpaRepository<PlaceBookmark, Long> {

  Optional<PlaceBookmark> findByUserIdAndPlace(Long userId, Place place);
}
