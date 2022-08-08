package com.prgrms.p2p.domain.course.util;

import com.prgrms.p2p.domain.course.dto.CoursePlaceResponse;
import com.prgrms.p2p.domain.course.dto.CreateCoursePlaceRequest;
import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.course.entity.CoursePlace;
import com.prgrms.p2p.domain.place.entity.Address;
import com.prgrms.p2p.domain.place.entity.Place;

public class CoursePlaceConverter {

  public static CoursePlace toCoursePlace(CreateCoursePlaceRequest createCoursePlaceRequest,
      Integer index, String imageUrl, Course course, Place place) {
    return new CoursePlace(index, createCoursePlaceRequest.getDescription(), imageUrl,
        createCoursePlaceRequest.getIsRecommended(), createCoursePlaceRequest.getIsThumbnail(),
        course, place);
  }

  public static Place toPlace(CreateCoursePlaceRequest createCoursePlaceRequest) {
    return new Place(createCoursePlaceRequest.getKakaoMapId(), createCoursePlaceRequest.getName(),
        new Address(createCoursePlaceRequest.getAddressName(),
            createCoursePlaceRequest.getRoadAddressName()), createCoursePlaceRequest.getLatitude(),
        createCoursePlaceRequest.getLongitude(), createCoursePlaceRequest.getCategory(),
        createCoursePlaceRequest.getPhoneNumber());
  }

  public static CoursePlaceResponse of(CoursePlace coursePlace) {
    return CoursePlaceResponse.builder().id(coursePlace.getId())
        .kakaoMapId(coursePlace.getPlace().getKakaoMapId()).name(coursePlace.getPlace().getName())
        .description(coursePlace.getDescription())
        .address(coursePlace.getPlace().getAddress().getRoadAddressName())
        .latitude(coursePlace.getPlace().getLatitude())
        .longitude(coursePlace.getPlace().getLongitude())
        .category(coursePlace.getPlace().getCategory())
        .phoneNumber(coursePlace.getPlace().getPhoneNumber().getNumber())
        .imageUrl(coursePlace.getImageUrl()).isRecommended(coursePlace.getRecommended())
        .isThumbnail(coursePlace.getThumbnailed()).category(coursePlace.getPlace().getCategory())
        .build();
  }
}
