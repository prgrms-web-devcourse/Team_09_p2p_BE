package com.prgrms.p2p.domain.course.util;

import com.prgrms.p2p.domain.course.dto.CreateCourseRequest;
import com.prgrms.p2p.domain.course.dto.DetailCourseResponse;
import com.prgrms.p2p.domain.course.dto.SummaryCourseResponse;
import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.user.entity.User;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CourseConverter {

  public static Course toCourse(CreateCourseRequest createCourseRequest, User user) {
    return new Course(createCourseRequest.getTitle(), createCourseRequest.getPeriod(),
        createCourseRequest.getRegion(), createCourseRequest.getDescription(),
        Set.copyOf(createCourseRequest.getThemes()), Set.copyOf(createCourseRequest.getSpots()),
        user);
  }

  public static DetailCourseResponse ofDetail(Course course, Boolean isLiked,
      Boolean isBookmarked) {
    return DetailCourseResponse.builder().id(course.getId()).title(course.getTitle())
        .thumbnail(null).region(course.getRegion()).period(course.getPeriod())
        .themes(List.copyOf(course.getThemes())).spots(List.copyOf(course.getSpots()))
        .places(course.getCoursePlaces()).likes(course.getCourseLikes().size()).isLiked(isLiked)
        .isBookmarked(isBookmarked).nickname(course.getUser().getNickname())
        .profileImage(course.getUser().getProfileUrl().orElse(null))
        .createdAt(course.getCreatedAt()).updatedAt(course.getUpdatedAt()).build();
  }

  public static SummaryCourseResponse ofSummary(Course course, Boolean isBookmarked) {
    return SummaryCourseResponse.builder().id(course.getId()).title(course.getTitle())
        .thumbnail(null).region(course.getRegion()).period(course.getPeriod())
        .themes(List.copyOf(course.getThemes()))
        .places(course.getCoursePlaces().stream().map(CoursePlaceConverter::of).collect(
            Collectors.toList()))
        .likeCount(course.getCourseLikes().size()).isBookmarked(isBookmarked)
        .nickname(course.getUser().getNickname()).build();
  }

}
