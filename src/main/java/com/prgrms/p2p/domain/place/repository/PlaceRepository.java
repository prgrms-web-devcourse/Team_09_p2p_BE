package com.prgrms.p2p.domain.place.repository;

import com.prgrms.p2p.domain.place.entity.Place;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long>, PlaceSearchRepository{

  Optional<Place> findByKakaoMapId(String kakaoMapId);
}