package com.prgrms.p2p.domain.course.service;

import static com.prgrms.p2p.domain.course.util.CourseConverter.toCourse;

import com.prgrms.p2p.domain.common.service.UploadService;
import com.prgrms.p2p.domain.course.dto.CreateCourseRequest;
import com.prgrms.p2p.domain.course.dto.UpdateCourseRequest;
import com.prgrms.p2p.domain.course.entity.Course;
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
    User user = userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);
    Course course = courseRepository.save(toCourse(createCourseRequest, user));
    saveCoursePlaces(createCourseRequest, images, course);
    return course.getId();
  }

  public Long countByUserId(Long userId) {
    return courseRepository.countByUserId(userId);
  }

  @Transactional
  public Long modify(Long courseId, UpdateCourseRequest updateCourseRequest,
      List<MultipartFile> newImages, Long userId) {
    User user = userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);
    Course course = courseRepository.findById(courseId).orElseThrow(IllegalArgumentException::new);
    updateCourse(course, updateCourseRequest);
    updateCoursePlaces(updateCourseRequest, newImages, course);
    return courseId;
  }

  private void saveCoursePlaces(CreateCourseRequest createCourseRequest, List<MultipartFile> images,
      Course course) {
    IntStream.range(0, createCourseRequest.getPlaces().size()).forEach(index -> {
      String url = uploadService.uploadImg(images.get(index));
      coursePlaceService.save(createCourseRequest.getPlaces().get(index), index, url, course);
    });
  }

  private void updateCoursePlaces(UpdateCourseRequest updateCourseRequest,
      List<MultipartFile> images, Course course) {
    AtomicInteger idx = new AtomicInteger();
    IntStream.range(0, updateCourseRequest.getPlaces().size()).forEach(index -> {
      String imageUrl = Objects.isNull(updateCourseRequest.getPlaces().get(index).getImageUrl())
          ? uploadService.uploadImg(images.get(idx.getAndIncrement()))
          : updateCourseRequest.getPlaces().get(index).getImageUrl();
      coursePlaceService.modify(updateCourseRequest.getPlaces().get(index), index, imageUrl,
          course);
    });
  }

  private void updateCourse(Course course, UpdateCourseRequest updateCourseRequest) {
    course.changeTitle(updateCourseRequest.getTitle());
    course.changePeriod(updateCourseRequest.getPeriod());
    course.changeRegion(updateCourseRequest.getRegion());
    course.changeThemes(updateCourseRequest.getThemes());
    course.changeSpots(updateCourseRequest.getSpots());
  }
}
