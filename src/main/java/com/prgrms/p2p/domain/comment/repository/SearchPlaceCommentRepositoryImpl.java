package com.prgrms.p2p.domain.comment.repository;

import static com.prgrms.p2p.domain.comment.entity.QPlaceComment.placeComment;

import com.prgrms.p2p.domain.comment.dto.PlaceCommentForQueryDsl;
import com.prgrms.p2p.domain.comment.entity.Visibility;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;

public class SearchPlaceCommentRepositoryImpl implements
    SearchPlaceCommentRepository {

  private final JPAQueryFactory jpaQueryFactory;

  public SearchPlaceCommentRepositoryImpl(EntityManager em) {
    this.jpaQueryFactory = new JPAQueryFactory(em);
  }

  @Override
  public Long findSubCommentCount(Long commentId) {
    return jpaQueryFactory.select(Wildcard.count)
        .from(placeComment)
        .where(placeComment.rootCommentId.eq(commentId),
            placeComment.visibility.eq(Visibility.TRUE)
        )
        .fetch()
        .get(0);
  }

  @Override
  public List<PlaceCommentForQueryDsl> findPlaceComments(Long placeId) {

    return null;
  }
}
