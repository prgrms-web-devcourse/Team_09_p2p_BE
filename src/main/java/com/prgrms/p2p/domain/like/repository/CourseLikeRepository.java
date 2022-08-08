package com.prgrms.p2p.domain.like.repository;

import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.like.entity.CourseLike;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseLikeRepository extends JpaRepository<CourseLike, Long> {

  Optional<CourseLike> findByUserIdAndCourse(Long userId, Course course);

  Boolean existsByUserIdAndCourse(Long userId, Course course);

}
