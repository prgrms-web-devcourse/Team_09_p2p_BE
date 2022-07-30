package com.prgrms.p2p.domain.course.repository;

import com.prgrms.p2p.domain.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoursePlaceRepository extends JpaRepository<Course, Long> {

}
