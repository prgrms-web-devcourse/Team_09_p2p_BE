package com.prgrms.p2p.domain.comment.repository;

import static com.prgrms.p2p.domain.comment.entity.QCourseComment.courseComment;
import static com.prgrms.p2p.domain.course.entity.QCourse.course;
import static com.prgrms.p2p.domain.user.entity.QUser.*;

import com.prgrms.p2p.domain.comment.dto.CourseCommentDto;
import com.prgrms.p2p.domain.comment.entity.Visibility;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;

public class SearchCourseCommentRepositoryImpl implements SearchCourseCommentRepository {

  private final JPAQueryFactory jpaQueryFactory;

  public SearchCourseCommentRepositoryImpl(EntityManager em) {
    this.jpaQueryFactory = new JPAQueryFactory(em);
  }

  @Override
  public List<CourseCommentDto> findCourseComment(Long courseId) {
    NumberExpression<Long> otherwise = new CaseBuilder().when(courseComment.rootCommentId.isNull())
        .then(courseComment.id)
        .otherwise(courseComment.rootCommentId);

    List<CourseCommentDto> courseCommentList = jpaQueryFactory.select(
            Projections.constructor(CourseCommentDto.class,
                courseComment.id,
                courseComment.comment,
                courseComment.rootCommentId,
                courseComment.course.id,
                courseComment.createdAt,
                courseComment.updatedAt,
                courseComment.visibility,
                user.id,
                user.nickname,
                user.profileUrl
            )
        )
        .from(courseComment)
        .leftJoin(courseComment.course, course)
        .leftJoin(user).on(courseComment.userId.eq(user.id))
        .where(course.id.eq(courseId),
            courseComment.visibility.eq(Visibility.TRUE)
                .or(courseComment.visibility.eq(Visibility.DELETED_INFORMATION)))
        .orderBy(otherwise.asc())
        .orderBy(courseComment.seq.asc())
        .fetch();

    return courseCommentList;
  }

  @Override
  public Long checkSubComment(Long commentId) {
    return jpaQueryFactory.select(Wildcard.count)
        .from(courseComment)
        .where(courseComment.rootCommentId.eq(commentId),
            courseComment.visibility.eq(Visibility.TRUE)
            )
        .fetch()
        .get(0);
  }
}