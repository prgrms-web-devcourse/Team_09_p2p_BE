package com.prgrms.p2p.domain.like.service;

import com.prgrms.p2p.domain.common.exception.NotFoundException;
import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.course.repository.CourseRepository;
import com.prgrms.p2p.domain.like.dto.LikeResponse;
import com.prgrms.p2p.domain.like.entity.CourseLike;
import com.prgrms.p2p.domain.like.repository.CourseLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CourseLikeService implements LikeService {

  private final CourseRepository courseRepository;
  private final CourseLikeRepository courseLikeRepository;

  @Override
  @Transactional
  public LikeResponse toggle(Long userId, Long courseId) {
    Course course = courseRepository.findById(courseId)
        .orElseThrow(() -> new NotFoundException("존재하지 않는 코스에 좋아요를 시도했습니다."));
    LikeResponse response = new LikeResponse();
    response.setId(courseId);
    courseLikeRepository.findByUserIdAndCourse(userId, course)
        .ifPresentOrElse(courseLike -> {
          dislike(courseLike);
          response.setIsLiked(Boolean.FALSE);
        }, () -> {
          like(userId, course);
          response.setIsLiked(Boolean.TRUE);
        });
    return response;
  }

  @Override
  public Long countByUserId(Long userId) {
    return courseLikeRepository.countByUserId(userId);
  }

  private void like(Long userId, Course course) {
    CourseLike courseLike = new CourseLike(userId, course);
    courseLikeRepository.save(courseLike);
  }

  private void dislike(CourseLike courseLike) {
    courseLike.deleteCourse(courseLike.getCourse());
    courseLikeRepository.delete(courseLike);
  }
}
