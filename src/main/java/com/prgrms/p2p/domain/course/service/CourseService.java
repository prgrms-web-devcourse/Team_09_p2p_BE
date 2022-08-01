package com.prgrms.p2p.domain.course.service;

import static com.prgrms.p2p.domain.course.util.CourseConverter.toCourse;

import com.prgrms.p2p.domain.common.service.UploadService;
import com.prgrms.p2p.domain.course.dto.CreateCoursePlaceRequest;
import com.prgrms.p2p.domain.course.dto.CreateCourseRequest;
import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.course.repository.CoursePlaceRepository;
import com.prgrms.p2p.domain.course.repository.CourseRepository;
import com.prgrms.p2p.domain.course.util.CoursePlaceConverter;
import com.prgrms.p2p.domain.place.entity.Place;
import com.prgrms.p2p.domain.place.repository.PlaceRepository;
import com.prgrms.p2p.domain.user.entity.User;
import com.prgrms.p2p.domain.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CourseService {

  private final UploadService uploadService;
  private final CourseRepository courseRepository;
  private final CoursePlaceRepository coursePlaceRepository;
  private final PlaceRepository placeRepository;
  private final UserRepository userRepository;

  @Transactional
  public Long save(CreateCourseRequest createCourseRequest, List<MultipartFile> images,
      Long userId) {
    User user = userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);
    Course course = courseRepository.save(toCourse(createCourseRequest, user));
    List<Place> places = getOrSavePlaces(createCourseRequest.getPlaces());
    saveCoursePlaces(createCourseRequest, course, places, images);
    return course.getId();
  }

  private void saveCoursePlaces(CreateCourseRequest createCourseRequest, Course course,
      List<Place> places, List<MultipartFile> images) {
    IntStream.range(0, places.size()).mapToObj(index -> {
      String url = uploadService.uploadImg(images.get(index));
      return coursePlaceRepository.save(
          CoursePlaceConverter.toCoursePlace(createCourseRequest.getPlaces().get(index), index, url,
              course, places.get(index)));
    });
  }

  private List<Place> getOrSavePlaces(List<CreateCoursePlaceRequest> createCoursePlaceRequests) {
    return IntStream.range(0, createCoursePlaceRequests.size()).mapToObj(index -> {
      CreateCoursePlaceRequest createCoursePlaceRequest = createCoursePlaceRequests.get(index);
      return CoursePlaceConverter.toPlace(createCoursePlaceRequest);
    }).collect(Collectors.toList()).stream().map(
        place -> placeRepository.findByKakaoMapId(place.getKakaoMapId())
            .orElseGet(() -> placeRepository.save(place))).collect(Collectors.toList());
  }

}
