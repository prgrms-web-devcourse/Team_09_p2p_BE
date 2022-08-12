package com.prgrms.p2p.domain.course.repository;

import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.course.entity.CoursePlace;
import com.prgrms.p2p.domain.place.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoursePlaceRepository extends JpaRepository<CoursePlace, Long> {

  void deleteBySeqAndCourseAndPlace(int seq, Course course, Place place);
}
