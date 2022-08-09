package com.prgrms.p2p.domain.comment.repository;

import static com.prgrms.p2p.domain.comment.entity.QCourseComment.courseComment;

import com.prgrms.p2p.domain.comment.entity.Visibility;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class SearchPlaceCommentRepositoryImpl implements
    SearchPlaceCommentRepository {

  private final JPAQueryFactory jpaQueryFactory;

  public SearchPlaceCommentRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
    this.jpaQueryFactory = jpaQueryFactory;
  }

  @Override
  public Long findSubCommentCount(Long commentId) {
    return jpaQueryFactory.select(Wildcard.count)
        .from(courseComment)
        .where(courseComment.rootCommentId.eq(commentId),
            courseComment.visibility.eq(Visibility.TRUE)
        )
        .fetch()
        .get(0);
  }
}
