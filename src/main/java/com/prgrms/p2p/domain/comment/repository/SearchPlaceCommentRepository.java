package com.prgrms.p2p.domain.comment.repository;

public interface SearchPlaceCommentRepository {

  Long findSubCommentCount(Long commentId);
}
