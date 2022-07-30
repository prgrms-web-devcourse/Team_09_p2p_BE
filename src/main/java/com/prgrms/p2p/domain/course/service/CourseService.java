package com.prgrms.p2p.domain.course.service;

import static com.prgrms.p2p.domain.place.util.PlaceConverter.toPlace;

import com.prgrms.p2p.domain.course.dto.CreateCoursePlaceRequest;
import com.prgrms.p2p.domain.course.dto.CreateCourseRequest;
import com.prgrms.p2p.domain.course.repository.CourseRepository;
import com.prgrms.p2p.domain.place.dto.CreatePlaceRequest;
import com.prgrms.p2p.domain.place.entity.Place;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CourseService {

  private final CourseRepository courseRepository;

  @Transactional
  public Long save(CreateCourseRequest createCourseRequest) {
    List<CreateCoursePlaceRequest> places = createCourseRequest.getPlaces();
    return courseRepository.save(place).getId();
  }
}
