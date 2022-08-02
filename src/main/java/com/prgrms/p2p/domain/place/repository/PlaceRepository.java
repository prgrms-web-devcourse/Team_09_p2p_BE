package com.prgrms.p2p.domain.place.repository;

import com.prgrms.p2p.domain.place.entity.Place;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlaceRepository extends JpaRepository<Place, Long>, PlaceSearchRepository{

  Optional<Place> findByKakaoMapId(String kakaoMapId);

  @Query("select p from Place p left join fetch p.placeBookmarks pb where pb.userId = :userId")
  Slice<Place> findBookmarkedPlace(@Param("userId") Long userId, Pageable pageable);
}