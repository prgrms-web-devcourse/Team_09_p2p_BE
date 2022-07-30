package com.prgrms.p2p.domain.course.service;

import com.prgrms.p2p.domain.course.dto.CreateCourseRequest;
import com.prgrms.p2p.domain.course.repository.CourseRepository;
import com.prgrms.p2p.domain.course.util.CoursePlaceConverter;
import com.prgrms.p2p.domain.place.entity.Place;
import com.prgrms.p2p.domain.place.repository.PlaceRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CourseService {

  private final CourseRepository courseRepository;
  private final PlaceRepository placeRepository;

  @Transactional
  public Long save(CreateCourseRequest createCourseRequest) {
    List<Place> places = createCourseRequest.getPlaces().stream().map(CoursePlaceConverter::toPlace)
        .collect(Collectors.toList());
    List<Place> savedPlaces = placeRepository.saveAll(places);
    courseRepository.save(place).getId();
  }

}
