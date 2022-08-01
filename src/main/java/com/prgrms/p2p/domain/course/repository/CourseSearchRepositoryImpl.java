package com.prgrms.p2p.domain.course.repository;

import static com.prgrms.p2p.domain.bookmark.entity.QCourseBookmark.courseBookmark;
import static com.prgrms.p2p.domain.course.entity.QCourse.course;

import com.prgrms.p2p.domain.course.dto.SearchCourseRequest;
import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.course.entity.Region;
import com.prgrms.p2p.domain.course.entity.Spot;
import com.prgrms.p2p.domain.course.entity.Theme;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;

public class CourseSearchRepositoryImpl implements CourseSearchRepository {

  private final JPAQueryFactory jpaQueryFactory;

  public CourseSearchRepositoryImpl(EntityManager em) {
    this.jpaQueryFactory = new JPAQueryFactory(em);
  }

  @Override
  public Slice<Course> searchCourse(SearchCourseRequest request, Pageable pageable) {
    JPAQuery<Course> courseJPAQuery = jpaQueryFactory.select(course).from(course)
        .leftJoin(course.courseBookmarks, courseBookmark).fetchJoin()
        .where(keywordListContains(request.getKeyword()), regionEq(request.getRegion()),
            themeEq(request.getThemes()), spotEq(request.getSpots())).offset(pageable.getOffset())
        .limit(pageable.getPageSize() + 1);

    for (Sort.Order o : pageable.getSort()) {
      PathBuilder pathBuilder = new PathBuilder<>(course.getType(), course.getMetadata());
      courseJPAQuery.orderBy(new OrderSpecifier(o.isAscending() ? Order.ASC : Order.DESC,
          pathBuilder.get(o.getProperty())));
    }
    List<Course> courses = courseJPAQuery.fetch();

    boolean hasNext = false;

    if (courses.size() > pageable.getPageSize()) {
      courses.remove(pageable.getPageSize());
      hasNext = true;
    }

    return new SliceImpl<>(courses, pageable, hasNext);
  }

  private BooleanBuilder keywordListContains(String keyword) {
    if (ObjectUtils.isEmpty(keyword)) {
      return null;
    }
    BooleanBuilder builder = new BooleanBuilder();
    String[] splitedKeyword = keyword.split(" ");
    for (String value : splitedKeyword) {
      builder.and(course.title.containsIgnoreCase(value));
    }
    return builder;
  }

  private BooleanExpression regionEq(Region region) {
    return ObjectUtils.isEmpty(region) ? null : course.region.eq(region);
  }

  private BooleanBuilder spotEq(List<Spot> spots) {
    if (ObjectUtils.isEmpty(spots)) {
      return null;
    }
    BooleanBuilder builder = new BooleanBuilder();

    for (Spot spot : spots) {
      builder.or(course.spots.any().eq(spot));
    }
    return builder;
  }

  private BooleanBuilder themeEq(List<Theme> themes) {
    if (ObjectUtils.isEmpty(themes)) {
      return null;
    }
    BooleanBuilder builder = new BooleanBuilder();
    for (Theme theme : themes) {
      builder.or(course.themes.any().eq(theme));
    }
    return builder;
  }
}