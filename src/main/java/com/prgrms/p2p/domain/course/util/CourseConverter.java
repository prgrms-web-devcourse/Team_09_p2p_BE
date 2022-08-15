package com.prgrms.p2p.domain.course.util;

import com.prgrms.p2p.domain.course.dto.CreateCourseRequest;
import com.prgrms.p2p.domain.course.dto.DetailCourseResponse;
import com.prgrms.p2p.domain.course.dto.SummaryCourseResponse;
import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.course.entity.CoursePlace;
import com.prgrms.p2p.domain.user.entity.User;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CourseConverter {

  public static Course toCourse(CreateCourseRequest createCourseRequest, User user) {
    return new Course(createCourseRequest.getTitle(), createCourseRequest.getPeriod(),
        createCourseRequest.getRegion(), Set.copyOf(createCourseRequest.getThemes()),
        Set.copyOf(createCourseRequest.getSpots()), user);
  }

  public static DetailCourseResponse ofDetail(Course course, Boolean isLiked, Boolean isBookmarked,
      Integer comments) {
    return DetailCourseResponse.builder().id(course.getId()).title(course.getTitle())
        .thumbnail(pickThumbnail(course)).region(course.getRegion()).period(course.getPeriod())
        .themes(List.copyOf(course.getThemes())).spots(List.copyOf(course.getSpots())).places(
            course.getCoursePlaces().stream().sorted(Comparator.comparing(CoursePlace::getSeq))
                .map(CoursePlaceConverter::of).collect(Collectors.toList()))
        .likes(course.getCourseLikes().size()).comments(comments).isLiked(isLiked)
        .isBookmarked(isBookmarked).userId(course.getUser().getId())
        .nickname(course.getUser().getNickname())
        .profileImage(course.getUser().getProfileUrl().orElse(null))
        .createdAt(course.getCreatedAt()).updatedAt(course.getUpdatedAt()).build();
  }

  public static SummaryCourseResponse ofSummary(Course course, Boolean isBookmarked) {
    return SummaryCourseResponse.builder().id(course.getId()).title(course.getTitle())
        .thumbnail(pickThumbnail(course)).region(course.getRegion()).period(course.getPeriod())
        .themes(List.copyOf(course.getThemes())).spots(List.copyOf(course.getSpots())).places(
            course.getCoursePlaces().stream().sorted(Comparator.comparing(CoursePlace::getSeq))
                .map(coursePlace -> coursePlace.getPlace().getName()).collect(Collectors.toList()))
        .likes(course.getCourseLikes().size()).isBookmarked(isBookmarked)
        .nickname(course.getUser().getNickname())
        .profileImage(course.getUser().getProfileUrl().orElse(null)).build();
  }

  public static String pickThumbnail(Course course) {
    return course.getCoursePlaces().stream().filter(CoursePlace::getThumbnailed).findFirst()
        .map(CoursePlace::getImageUrl).orElseGet(
            () -> course.getCoursePlaces().size() > 0 ? course.getCoursePlaces().get(0)
                .getImageUrl() : null);
  }
}
