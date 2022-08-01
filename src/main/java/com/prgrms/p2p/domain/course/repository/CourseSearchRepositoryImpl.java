package com.prgrms.p2p.domain.course.repository;

import com.prgrms.p2p.domain.course.dto.SearchCourseRequest;
import com.prgrms.p2p.domain.course.entity.Course;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

@RequiredArgsConstructor
public class CourseSearchRepositoryImpl implements CourseSearchRepository {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Slice<Course> searchCourse(SearchCourseRequest searchCourseRequest, Pageable pageable) {
    return null;
  }
}
