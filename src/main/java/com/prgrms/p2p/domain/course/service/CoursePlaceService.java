package com.prgrms.p2p.domain.course.service;

import com.prgrms.p2p.domain.course.dto.CreateCoursePlaceRequest;
import com.prgrms.p2p.domain.course.dto.UpdateCoursePlaceRequest;
import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.course.entity.CoursePlace;
import com.prgrms.p2p.domain.course.repository.CoursePlaceRepository;
import com.prgrms.p2p.domain.course.util.CoursePlaceConverter;
import com.prgrms.p2p.domain.place.entity.Place;
import com.prgrms.p2p.domain.place.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CoursePlaceService {

  private final CoursePlaceRepository coursePlaceRepository;
  private final PlaceService placeService;

  public void save(CreateCoursePlaceRequest createCoursePlaceRequest, Integer index,
      String imageUrl, Course course) {

    Place place = placeService.findAndUpdateExistPlace(createCoursePlaceRequest
        .orElseGet(() -> placeService.save(createCoursePlaceRequest));

    CoursePlace coursePlace = CoursePlaceConverter.toCoursePlace(createCoursePlaceRequest, index,
        imageUrl, course, place);

    coursePlaceRepository.save(coursePlace);
  }

  public void modify(UpdateCoursePlaceRequest updateCoursePlaceRequest, Integer index,
      String imageUrl, Course course) {
    Place place = placeService.findAndUpdateExistPlace(updateCoursePlaceRequest)
        .orElseGet(() -> placeService.save(updateCoursePlaceRequest));

    CoursePlace coursePlace = CoursePlaceConverter.toCoursePlace(updateCoursePlaceRequest, index,
        updateCoursePlaceRequest.getImageUrl(), course, place);

    coursePlaceRepository.save(coursePlace);
  }
}