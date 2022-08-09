package com.prgrms.p2p.domain.place.repository;

import com.prgrms.p2p.domain.place.dto.SearchPlaceDto;
import com.prgrms.p2p.domain.place.entity.Place;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PlaceSearchRepository {

  Slice<Place> searchPlace(SearchPlaceDto searchPlaceDto, Pageable pageable);
}
