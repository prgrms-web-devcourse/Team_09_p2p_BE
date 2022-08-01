package com.prgrms.p2p.domain.course.service;

import com.prgrms.p2p.domain.course.dto.DetailCourseResponse;
import com.prgrms.p2p.domain.course.dto.SearchCourseRequest;
import com.prgrms.p2p.domain.course.dto.SummaryCourseResponse;
import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.course.repository.CourseRepository;
import com.prgrms.p2p.domain.course.util.CourseConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CourseQueryService {

  private final CourseRepository courseRepository;

  public DetailCourseResponse findDetail(Long id, Long userId) {
    Course course = courseRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    return CourseConverter.ofDetail(course, true, true);
  }

  public SummaryCourseResponse findSummaryList(SearchCourseRequest searchCourseRequest,
      Pageable pageable) {

    return null;
  }


}
