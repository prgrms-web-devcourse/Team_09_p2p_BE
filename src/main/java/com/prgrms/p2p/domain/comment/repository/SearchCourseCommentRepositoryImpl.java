package com.prgrms.p2p.domain.comment.repository;

import static com.prgrms.p2p.domain.comment.entity.QCourseComment.courseComment;
import static com.prgrms.p2p.domain.course.entity.QCourse.course;
import static com.prgrms.p2p.domain.user.entity.QUser.*;

import com.prgrms.p2p.domain.comment.dto.CourseCommentResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

public class SearchCourseCommentRepositoryImpl implements SearchCourseCommentRepository {

  private final JPAQueryFactory jpaQueryFactory;

  public SearchCourseCommentRepositoryImpl(EntityManager em) {
    this.jpaQueryFactory = new JPAQueryFactory(em);
  }

  @Override
  public Slice<CourseCommentResponse> findCourseComment(Long courseId, Pageable pageable) {
    NumberExpression<Long> otherwise = new CaseBuilder().when(courseComment.rootCommentId.isNull())
        .then(courseComment.id)
        .otherwise(courseComment.rootCommentId);

    List<CourseCommentResponse> courseCommentRes = jpaQueryFactory.select(
            Projections.constructor(CourseCommentResponse.class,
                courseComment.id,
                courseComment.comment,
                courseComment.rootCommentId,
                courseComment.course.id,
                courseComment.createdAt,
                courseComment.updatedAt,
                user.id,
                user.nickname,
                user.profileUrl
            )
        )
        .from(courseComment)
        .leftJoin(courseComment.course, course)
        .leftJoin(user).on(courseComment.userId.eq(user.id))
        .where(course.id.eq(courseId))
        .orderBy(otherwise.asc())
        .orderBy(courseComment.seq.asc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize() + 1)
        .fetch();

    boolean hasNext = false;

    if (courseCommentRes.size() > pageable.getPageSize()) {
      courseCommentRes.remove(pageable.getPageSize());
      hasNext = true;
    }

    return new SliceImpl<>(courseCommentRes, pageable, hasNext);
  }
}