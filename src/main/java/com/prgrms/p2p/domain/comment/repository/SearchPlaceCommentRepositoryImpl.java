package com.prgrms.p2p.domain.comment.repository;

import static com.prgrms.p2p.domain.comment.entity.QPlaceComment.placeComment;
import static com.prgrms.p2p.domain.user.entity.QUser.*;

import com.prgrms.p2p.domain.comment.dto.CourseCommentForQueryDsl;
import com.prgrms.p2p.domain.comment.dto.PlaceCommentForQueryDsl;
import com.prgrms.p2p.domain.comment.entity.QPlaceComment;
import com.prgrms.p2p.domain.comment.entity.Visibility;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
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
    NumberExpression<Long> otherwise = new CaseBuilder().when(placeComment.rootCommentId.isNull())
        .then(placeComment.id)
        .otherwise(placeComment.rootCommentId);

    return jpaQueryFactory.select(
            Projections.constructor(PlaceCommentForQueryDsl.class,
                placeComment.id,
                placeComment.comment,
                placeComment.rootCommentId,
                placeComment.createdAt,
                placeComment.updatedAt,
                placeComment.visibility,
                user.id,
                user.nickname,
                user.profileUrl
            )
        )
        .from(placeComment)
        .leftJoin(user).on(placeComment.userId.eq(user.id))
        .where(
            placeComment.place.id.eq(placeId),
            placeComment.visibility.eq(Visibility.TRUE)
                .or(placeComment.visibility.eq(Visibility.DELETED_INFORMATION))
        )
        .orderBy(otherwise.asc())
        .orderBy(placeComment.createdAt.asc())
        .fetch();
  }

  @Override
  public List<CourseCommentForQueryDsl> findPlaceCommentsByUserId(Long userId) {
    QPlaceComment commentSub = new QPlaceComment("commentSub");
    JPQLQuery<Long> count = JPAExpressions
        .select(commentSub.count())
        .from(commentSub)
        .where(commentSub.rootCommentId.eq(placeComment.id),
            commentSub.visibility.eq(Visibility.TRUE)
        );
    List<CourseCommentForQueryDsl> fetch = jpaQueryFactory.select(
            Projections.constructor(CourseCommentForQueryDsl.class,
                placeComment.id,
                placeComment.comment,
                placeComment.rootCommentId,
                placeComment.createdAt,
                placeComment.updatedAt,
                count,
                placeComment.place.id,
                placeComment.place.name,
                placeComment.userId
            ))
        .from(placeComment)
        .where(
            placeComment.userId.eq(userId),
            placeComment.visibility.eq(Visibility.TRUE))
        .fetch();
    return fetch;
  }
}
