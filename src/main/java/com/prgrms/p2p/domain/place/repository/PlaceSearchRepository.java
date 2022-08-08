package com.prgrms.p2p.domain.place.repository;

import com.prgrms.p2p.domain.place.dto.SearchPlaceRequest;
import com.prgrms.p2p.domain.place.entity.Place;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PlaceSearchRepository {

  Slice<Place> searchPlace(String keyword, Pageable pageable);
}
