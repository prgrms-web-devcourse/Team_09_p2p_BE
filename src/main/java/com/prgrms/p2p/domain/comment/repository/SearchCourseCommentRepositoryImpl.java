package com.prgrms.p2p.domain.comment.repository;

import static com.prgrms.p2p.domain.comment.entity.QCourseComment.courseComment;
import static com.prgrms.p2p.domain.user.entity.QUser.*;

import com.prgrms.p2p.domain.comment.dto.CourseCommentForQueryDsl;
import com.prgrms.p2p.domain.comment.entity.QCourseComment;
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

public class SearchCourseCommentRepositoryImpl implements SearchCourseCommentRepository {

  private final JPAQueryFactory jpaQueryFactory;

  public SearchCourseCommentRepositoryImpl(EntityManager em) {
    this.jpaQueryFactory = new JPAQueryFactory(em);
  }

  @Override
  public List<CourseCommentForQueryDsl> findCourseComment(Long courseId) {
    NumberExpression<Long> otherwise = new CaseBuilder().when(courseComment.rootCommentId.isNull())
        .then(courseComment.id)
        .otherwise(courseComment.rootCommentId);

    return jpaQueryFactory.select(
            Projections.constructor(CourseCommentForQueryDsl.class,
                courseComment.id,
                courseComment.comment,
                courseComment.rootCommentId,
                courseComment.createdAt,
                courseComment.updatedAt,
                courseComment.visibility,
                user.id,
                user.nickname,
                user.profileUrl
            )
        )
        .from(courseComment)
        .leftJoin(user).on(courseComment.userId.eq(user.id))
        .where(
            courseComment.course.id.eq(courseId),
            courseComment.visibility.eq(Visibility.TRUE)
                .or(courseComment.visibility.eq(Visibility.DELETED_INFORMATION)))
        .orderBy(otherwise.asc())
        .orderBy(courseComment.createdAt.asc())
        .fetch();
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

  @Override
  public List<CourseCommentForQueryDsl> findCourseCommentsByUserId(Long userId) {
    QCourseComment commentSub = new QCourseComment("commentSub");
    JPQLQuery<Long> count = JPAExpressions
        .select(commentSub.count())
        .from(commentSub)
        .where(commentSub.rootCommentId.eq(courseComment.id),
            commentSub.visibility.eq(Visibility.TRUE)
        );
    List<CourseCommentForQueryDsl> fetch = jpaQueryFactory.select(
            Projections.constructor(CourseCommentForQueryDsl.class,
                courseComment.id,
                courseComment.comment,
                courseComment.rootCommentId,
                courseComment.createdAt,
                courseComment.updatedAt,
                count,
                courseComment.course.id,
                courseComment.course.title,
                courseComment.userId
            ))
        .from(courseComment)
        .where(
            courseComment.userId.eq(userId),
            courseComment.visibility.eq(Visibility.TRUE))
        .fetch();
    return fetch;
  }

  @Override
  public Long countByUserId(Long userId) {
    return jpaQueryFactory.select(Wildcard.count)
        .from(courseComment)
        .where(courseComment.userId.eq(userId),
            courseComment.visibility.eq(Visibility.TRUE)
        )
        .fetch()
        .get(0);
  }
}