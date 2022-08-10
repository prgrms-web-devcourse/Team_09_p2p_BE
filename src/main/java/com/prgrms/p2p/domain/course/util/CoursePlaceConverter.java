package com.prgrms.p2p.domain.course.util;

import com.prgrms.p2p.domain.course.dto.CoursePlaceRequest;
import com.prgrms.p2p.domain.course.dto.CoursePlaceResponse;
import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.course.entity.CoursePlace;
import com.prgrms.p2p.domain.place.entity.Address;
import com.prgrms.p2p.domain.place.entity.Place;
import java.util.Objects;

public class CoursePlaceConverter {

  public static CoursePlace toCoursePlace(CoursePlaceRequest coursePlaceRequest,
      Integer index, String imageUrl, Course course, Place place) {
    return new CoursePlace(index, coursePlaceRequest.getDescription(), imageUrl,
        coursePlaceRequest.getIsRecommended(), coursePlaceRequest.getIsThumbnail(),
        course, place);
  }

  public static Place toPlace(CoursePlaceRequest coursePlaceRequest) {
    return new Place(coursePlaceRequest.getKakaoMapId(), coursePlaceRequest.getName(),
        new Address(coursePlaceRequest.getAddressName(),
            coursePlaceRequest.getRoadAddressName()), coursePlaceRequest.getLatitude(),
        coursePlaceRequest.getLongitude(), coursePlaceRequest.getCategory(),
        coursePlaceRequest.getPhoneNumber());
  }

  public static CoursePlaceResponse of(CoursePlace coursePlace) {
    return CoursePlaceResponse.builder().id(coursePlace.getId())
        .kakaoMapId(coursePlace.getPlace().getKakaoMapId()).name(coursePlace.getPlace().getName())
        .description(coursePlace.getDescription()).address(
            Objects.isNull(coursePlace.getPlace().getAddress().getRoadAddressName()) ? null
                : coursePlace.getPlace().getAddress().getRoadAddressName())
        .latitude(coursePlace.getPlace().getLatitude())
        .longitude(coursePlace.getPlace().getLongitude())
        .category(coursePlace.getPlace().getCategory()).phoneNumber(
            Objects.isNull(coursePlace.getPlace().getPhoneNumber()) ? null
                : coursePlace.getPlace().getPhoneNumber().getNumber())
        .imageUrl(coursePlace.getImageUrl()).isRecommended(coursePlace.getRecommended())
        .isThumbnail(coursePlace.getThumbnailed()).category(coursePlace.getPlace().getCategory())
        .build();
  }
}
