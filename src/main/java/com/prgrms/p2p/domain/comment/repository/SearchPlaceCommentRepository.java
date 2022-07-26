package com.prgrms.p2p.domain.comment.repository;

import com.prgrms.p2p.domain.comment.dto.CourseCommentForQueryDsl;
import com.prgrms.p2p.domain.comment.dto.PlaceCommentForQueryDsl;
import com.prgrms.p2p.domain.place.entity.Place;
import java.util.List;

public interface SearchPlaceCommentRepository {

  Long findSubCommentCount(Long commentId);
  List<PlaceCommentForQueryDsl> findPlaceComments(Long placeId);

  List<CourseCommentForQueryDsl> findPlaceCommentsByUserId(Long userId);

  Long countByPlace(Place place);
}
