package com.prgrms.p2p.domain.comment.repository;

import com.prgrms.p2p.domain.comment.entity.PlaceComment;
import com.prgrms.p2p.domain.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceCommentRepository extends JpaRepository<PlaceComment, Long>, SearchPlaceCommentRepository {

  Integer countByPlace(Place place);

}