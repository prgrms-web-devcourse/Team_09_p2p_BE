package com.prgrms.p2p.domain.course.service;

import static com.prgrms.p2p.domain.course.util.CourseConverter.toCourse;

import com.prgrms.p2p.domain.common.exception.NotFoundException;
import com.prgrms.p2p.domain.common.exception.UnAuthorizedException;
import com.prgrms.p2p.domain.common.service.UploadService;
import com.prgrms.p2p.domain.course.dto.CreateCourseRequest;
import com.prgrms.p2p.domain.course.dto.UpdateCourseRequest;
import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.course.exception.CoursePlaceAndImageSizeNotEqualException;
import com.prgrms.p2p.domain.course.repository.CourseRepository;
import com.prgrms.p2p.domain.user.entity.User;
import com.prgrms.p2p.domain.user.repository.UserRepository;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseService {

  private final UploadService uploadService;
  private final CourseRepository courseRepository;
  private final UserRepository userRepository;
  private final CoursePlaceService coursePlaceService;

  @Transactional
  public Long save(CreateCourseRequest createCourseRequest, List<MultipartFile> images,
      Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException("존재하지 않는 유저입니다."));
    Course course = courseRepository.save(toCourse(createCourseRequest, user));
    saveCoursePlaces(createCourseRequest, images, course);
    return course.getId();
  }


  @Transactional
  public Long modify(Long courseId, UpdateCourseRequest updateCourseRequest,
      List<MultipartFile> newImages, Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NotFoundException("존재하지 않는 유저입니다."));
    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new NotFoundException("존재하지 않는 코스입니다."));
    validateAuthorization(userId, course);
    updateCourse(course, updateCourseRequest);
    updateCoursePlaces(updateCourseRequest, newImages, course);
    return courseId;
  }

  private void saveCoursePlaces(CreateCourseRequest createCourseRequest, List<MultipartFile> images,
      Course course) {
    if (createCourseRequest.getPlaces().size() == 0 || images.size() == 0
        || createCourseRequest.getPlaces().size() != images.size()) {
      throw new CoursePlaceAndImageSizeNotEqualException("저장하려는 코스 장소와 이미지 크기가 맞지 않습니다.");
    }
    IntStream.range(0, createCourseRequest.getPlaces().size()).forEach(index -> {
      String url = uploadService.uploadImg(images.get(index));
      coursePlaceService.save(createCourseRequest.getPlaces().get(index), index, url, course);
    });
  }

  private void updateCoursePlaces(UpdateCourseRequest updateCourseRequest,
      List<MultipartFile> images, Course course) {
    int coursePlaceSize = (int) updateCourseRequest.getPlaces().stream()
        .filter(updateCoursePlaceRequest -> Objects.isNull(updateCoursePlaceRequest.getImageUrl()))
        .count();
    if (coursePlaceSize != images.size()) {
      throw new CoursePlaceAndImageSizeNotEqualException("저장하려는 코스 장소와 이미지 크기가 맞지 않습니다.");
    }
    AtomicInteger imageOrder = new AtomicInteger();
    IntStream.range(0, updateCourseRequest.getPlaces().size()).forEach(index -> {
      String imageUrl = Objects.isNull(updateCourseRequest.getPlaces().get(index).getImageUrl())
          ? uploadService.uploadImg(images.get(imageOrder.getAndIncrement()))
          : updateCourseRequest.getPlaces().get(index).getImageUrl();
      coursePlaceService.modify(updateCourseRequest.getPlaces().get(index), index, imageUrl,
          course);
    });
  }

  public void deleteCourse(Long courseId, Long userId) {
    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new NotFoundException("존재하지 않는 코가입니다."));
    validateAuthorization(userId, course);
    courseRepository.delete(course);

  }

  private void validateAuthorization(Long userId, Course course) {
    if (!course.getUser().getId().equals(userId)) {
      throw new UnAuthorizedException("권한이 없습니다.");
    }
  }

  private void validationSize(int courseplaceSize, int imageSize) {
    if (courseplaceSize == 0 || imageSize == 0 || courseplaceSize != imageSize) {
      throw new CoursePlaceAndImageSizeNotEqualException("저장하려는 코스 장소와 이미지 크기가 맞지 않습니다.");
    }
  }

  private void updateCourse(Course course, UpdateCourseRequest updateCourseRequest) {
    course.changeTitle(updateCourseRequest.getTitle());
    course.changePeriod(updateCourseRequest.getPeriod());
    course.changeRegion(updateCourseRequest.getRegion());
    course.changeThemes(updateCourseRequest.getThemes());
    course.changeSpots(updateCourseRequest.getSpots());
  }
}
