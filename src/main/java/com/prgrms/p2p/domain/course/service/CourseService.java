package com.prgrms.p2p.domain.course.service;

import static com.prgrms.p2p.domain.course.util.CourseConverter.toCourse;

import com.prgrms.p2p.domain.common.service.UploadService;
import com.prgrms.p2p.domain.course.dto.CreateCourseRequest;
import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.course.repository.CourseRepository;
import com.prgrms.p2p.domain.user.entity.User;
import com.prgrms.p2p.domain.user.repository.UserRepository;
import java.util.List;
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

  private void saveCoursePlaces(CreateCourseRequest createCourseRequest, List<MultipartFile> images,
      Course course) {
    IntStream.range(0, createCourseRequest.getPlaces().size()).forEach(
        index -> {
          String url = uploadService.uploadImg(images.get(index));
          coursePlaceService.save(createCourseRequest.getPlaces().get(index), index, url, course);
        }
    );
  }
}
