package com.prgrms.p2p.domain.course.repository;

import com.prgrms.p2p.domain.course.entity.Course;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends JpaRepository<Course, Long>, CourseSearchRepository {

  @Query("select c from Course c left join fetch c.coursePlaces")
  Optional<Course> findById(@Param("courseId") Long courseId);
}
