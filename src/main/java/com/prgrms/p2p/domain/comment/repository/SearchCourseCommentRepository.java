package com.prgrms.p2p.domain.comment.repository;

import com.prgrms.p2p.domain.comment.dto.CourseCommentDto;
import java.util.List;

public interface SearchCourseCommentRepository {

  List<CourseCommentDto> findCourseComment(Long CourseId);

  Long checkSubComment(Long commentId);
}