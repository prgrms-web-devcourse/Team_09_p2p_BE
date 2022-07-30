package com.prgrms.p2p.domain.course.util;

import com.prgrms.p2p.domain.course.dto.CreateCoursePlaceRequest;
import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.course.entity.CoursePlace;
import com.prgrms.p2p.domain.place.entity.Address;
import com.prgrms.p2p.domain.place.entity.Place;

public class CoursePlaceConverter {

  public static CoursePlace toCoursePlace(CreateCoursePlaceRequest createCoursePlaceRequest,
      Integer index, Course course, Place place) {
    return new CoursePlace(index, createCoursePlaceRequest.getKakaoMapId(),
        createCoursePlaceRequest.getDescription(), null,
        createCoursePlaceRequest.getIsRecommended(), course, place);
  }

  public static Place toPlace(CreateCoursePlaceRequest createCoursePlaceRequest) {
    return new Place(createCoursePlaceRequest.getKakaoMapId(), createCoursePlaceRequest.getName(),
        new Address(createCoursePlaceRequest.getAddressName(),
            createCoursePlaceRequest.getRoadAddressName()), createCoursePlaceRequest.getLatitude(),
        createCoursePlaceRequest.getLongitude(), createCoursePlaceRequest.getCategory(),
        createCoursePlaceRequest.getPhoneNumber(), null);
  }
}
