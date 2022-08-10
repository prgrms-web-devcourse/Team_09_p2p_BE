package com.prgrms.p2p.domain.course.service;

import com.prgrms.p2p.domain.common.exception.NotFoundException;
import com.prgrms.p2p.domain.course.dto.CoursePlaceRequest;
import com.prgrms.p2p.domain.course.dto.UpdateCoursePlaceRequest;
import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.course.entity.CoursePlace;
import com.prgrms.p2p.domain.course.repository.CoursePlaceRepository;
import com.prgrms.p2p.domain.course.util.CoursePlaceConverter;
import com.prgrms.p2p.domain.place.entity.Place;
import com.prgrms.p2p.domain.place.service.PlaceService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CoursePlaceService {

  private final CoursePlaceRepository coursePlaceRepository;
  private final PlaceService placeService;

  public void save(CoursePlaceRequest coursePlaceRequest, Integer index, String imageUrl,
      Course course) {

    Place place = placeService.findAndUpdateExistPlace(coursePlaceRequest)
        .orElseGet(() -> placeService.save(coursePlaceRequest));

    CoursePlace coursePlace = CoursePlaceConverter.toCoursePlace(coursePlaceRequest, index,
        imageUrl, course, place);

    coursePlaceRepository.save(coursePlace);
  }

  public void modify(UpdateCoursePlaceRequest updateCoursePlaceRequest, Integer index,
      String imageUrl, Course course) {
    Place place = placeService.findAndUpdateExistPlace(updateCoursePlaceRequest)
        .orElseGet(() -> placeService.save(updateCoursePlaceRequest));
    CoursePlace coursePlace =
        Objects.isNull(updateCoursePlaceRequest.getCoursePlaceId()) ? coursePlaceRepository.save(
            CoursePlaceConverter.toCoursePlace(updateCoursePlaceRequest, index, imageUrl, course,
                place))
            : coursePlaceRepository.findById(updateCoursePlaceRequest.getCoursePlaceId())
                .orElseThrow(() -> new NotFoundException("존재하지 않는 코스 장소입니다."));
    updateCoursePlaces(updateCoursePlaceRequest, index, imageUrl, coursePlace);

  }

  private void updateCoursePlaces(UpdateCoursePlaceRequest updateCoursePlaceRequest, Integer index,
      String imageUrl, CoursePlace coursePlace) {
    coursePlace.changeSeq(index);
    coursePlace.changeDescription(updateCoursePlaceRequest.getDescription());
    coursePlace.changeImageUrl(imageUrl);
    coursePlace.changeRecommended(updateCoursePlaceRequest.getIsRecommended());
    coursePlace.changeThumbnailed(updateCoursePlaceRequest.getIsThumbnail());
  }
}