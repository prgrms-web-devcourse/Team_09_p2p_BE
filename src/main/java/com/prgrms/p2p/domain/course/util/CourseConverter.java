package com.prgrms.p2p.domain.course.util;

import com.prgrms.p2p.domain.course.dto.CreateCourseRequest;
import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.user.entity.User;
import java.util.Set;

public class CourseConverter {

  public static Course toCourse(CreateCourseRequest createCourseRequest, User user) {
    return new Course(createCourseRequest.getTitle(), createCourseRequest.getPeriod(),
        createCourseRequest.getRegion(), createCourseRequest.getDescription(),
        Set.copyOf(createCourseRequest.getThemes()), user);
  }

}
