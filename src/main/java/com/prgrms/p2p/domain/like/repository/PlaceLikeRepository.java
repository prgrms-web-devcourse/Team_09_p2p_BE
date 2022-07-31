package com.prgrms.p2p.domain.like.repository;

import com.prgrms.p2p.domain.like.entity.PlaceLike;
import com.prgrms.p2p.domain.place.entity.Place;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceLikeRepository extends JpaRepository<PlaceLike, Long> {

  Optional<PlaceLike> findPlaceLikeByUserIdAndPlace(Long userId, Place place);
}
