package com.prgrms.p2p.domain.comment.repository;

import com.prgrms.p2p.domain.comment.entity.CourseComment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface SearchCourseCommentRepository {

  Slice<CourseComment> findCourseComment(Long CourseId, Pageable pageable);

}