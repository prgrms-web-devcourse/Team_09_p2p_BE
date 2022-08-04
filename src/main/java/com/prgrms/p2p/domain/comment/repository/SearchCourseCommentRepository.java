package com.prgrms.p2p.domain.comment.repository;

import com.prgrms.p2p.domain.comment.dto.CourseCommentResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface SearchCourseCommentRepository {

  Slice<CourseCommentResponse> findCourseComment(Long CourseId, Pageable pageable);

}