package com.prgrms.p2p.domain.place.repository;

import com.prgrms.p2p.domain.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long>, PlaceSearchRepository{

}