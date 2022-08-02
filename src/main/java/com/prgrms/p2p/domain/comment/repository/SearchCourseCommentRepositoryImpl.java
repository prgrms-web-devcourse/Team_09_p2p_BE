package com.prgrms.p2p.domain.comment.repository;

import static com.prgrms.p2p.domain.comment.entity.QCourseComment.courseComment;

import com.prgrms.p2p.domain.comment.entity.CourseComment;
import com.prgrms.p2p.domain.course.entity.QCourse;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

public class SearchCourseCommentRepositoryImpl implements SearchCourseCommentRepository{

  private final JPAQueryFactory jpaQueryFactory;

  public SearchCourseCommentRepositoryImpl(EntityManager em) {
    this.jpaQueryFactory = new JPAQueryFactory(em);
  }

  @Override
  public Slice<CourseComment> findCourseComment(Long courseId, Pageable pageable) {
    NumberExpression<Long> otherwise = new CaseBuilder().when(courseComment.rootCommentId.isNull())
        .then(courseComment.id)
        .otherwise(courseComment.rootCommentId);

    List<CourseComment> commentList = jpaQueryFactory.select(courseComment)
        .from(courseComment)
        .leftJoin(courseComment.course, QCourse.course).fetchJoin()
        .where(QCourse.course.id.eq(courseId))
        .orderBy(otherwise.asc())
        .orderBy(courseComment.seq.asc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize() + 1)
        .fetch();

    boolean hasNext = false;

    if (commentList.size() > pageable.getPageSize()) {
      commentList.remove(pageable.getPageSize());
      hasNext = true;
    }

    return new SliceImpl<>(commentList, pageable, hasNext);
  }
}