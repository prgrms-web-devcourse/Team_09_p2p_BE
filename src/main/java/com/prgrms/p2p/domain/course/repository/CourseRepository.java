package com.prgrms.p2p.domain.course.repository;

import com.prgrms.p2p.domain.course.entity.Course;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends JpaRepository<Course, Long>, CourseSearchRepository {

  @Query("select c from Course c left join fetch c.courseBookmarks cb where (:userId is null or cb.userId = :userId)")
  Slice<Course> findBookmarkedCourse(@Param("userId") Long userId, Pageable pageable);

  Long countByUserId(Long userId);
}
