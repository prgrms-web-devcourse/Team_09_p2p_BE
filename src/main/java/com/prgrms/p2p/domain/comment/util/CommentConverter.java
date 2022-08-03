package com.prgrms.p2p.domain.comment.util;

import com.prgrms.p2p.domain.comment.dto.CreateCourseCommentRequest;
import com.prgrms.p2p.domain.comment.entity.CourseComment;
import com.prgrms.p2p.domain.course.entity.Course;

public class CommentConverter {

  public static CourseComment toCourseComment(
      CreateCourseCommentRequest createCourseCommentReq, Course course, Long seq) {
    Long rootCommentId
        = createCourseCommentReq.getRootCommentId().isEmpty() ?
        null : createCourseCommentReq.getRootCommentId().get();
    return new CourseComment(createCourseCommentReq.getComment(),
        rootCommentId,
        createCourseCommentReq.getUserId(),
        course,
        seq
    );

  }
}