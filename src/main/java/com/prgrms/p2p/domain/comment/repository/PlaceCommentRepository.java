package com.prgrms.p2p.domain.comment.repository;

import com.prgrms.p2p.domain.comment.entity.PlaceComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceCommentRepository extends JpaRepository<PlaceComment, Long>, SearchPlaceCommentRepository {

}