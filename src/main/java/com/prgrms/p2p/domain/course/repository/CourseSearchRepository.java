package com.prgrms.p2p.domain.course.repository;

import com.prgrms.p2p.domain.course.dto.SearchCourseRequest;
import com.prgrms.p2p.domain.course.entity.Course;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface CourseSearchRepository {

  Slice<Course> searchCourse(SearchCourseRequest searchCourseRequest, Pageable pageable);
}
