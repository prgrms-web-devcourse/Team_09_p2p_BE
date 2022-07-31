package com.prgrms.p2p.domain.course.service;

import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.course.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CourseQueryService {

  private final CourseRepository courseRepository;

  public void findDetail(Long id) {
    Course course = courseRepository.findById(id).orElseThrow(IllegalArgumentException::new);

  }
}
