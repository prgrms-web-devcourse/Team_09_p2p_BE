package com.prgrms.p2p.domain.comment.repository;

import com.prgrms.p2p.domain.comment.entity.CourseComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseCommentRepository extends JpaRepository<CourseComment, Long>,
    SearchCourseCommentRepository {

  @Query("select count (cc) from CourseComment cc where cc.rootCommentId =:rootCommentId")
  Long findSequence(@Param("rootCommentId") Long rootCommentId);

}