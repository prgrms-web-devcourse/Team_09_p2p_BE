package com.prgrms.p2p.domain.comment.util;

import com.prgrms.p2p.domain.comment.dto.CourseCommentDto;
import com.prgrms.p2p.domain.comment.dto.CourseCommentResponse;
import com.prgrms.p2p.domain.comment.dto.CourseCommentResponse.UserDto;
import com.prgrms.p2p.domain.comment.dto.CreateCourseCommentRequest;
import com.prgrms.p2p.domain.comment.entity.CourseComment;
import com.prgrms.p2p.domain.comment.entity.Visibility;
import com.prgrms.p2p.domain.course.entity.Course;

public class CommentConverter {

  public static CourseComment toCourseComment(
      CreateCourseCommentRequest createCourseCommentReq, Course course, Long seq, Long userId) {

    return new CourseComment(createCourseCommentReq.getComment(),
        createCourseCommentReq.getRootCommentId(),
        userId,
        course,
        seq
    );
  }

  public static CourseCommentResponse toCourseCommentResponse(CourseCommentDto courseCommentDto) {

    if (courseCommentDto.getVisibility().equals(Visibility.DELETED_INFORMATION)) {
      courseCommentDto.changeNoneComment();
    }

    return CourseCommentResponse.builder()
        .id(courseCommentDto.getId())
        .comment(courseCommentDto.getComment())
        .rootCommentId(courseCommentDto.getRootCommentId())
        .courseId(courseCommentDto.getCourseId())
        .createdAt(courseCommentDto.getCreatedAt())
        .updatedAt(courseCommentDto.getUpdatedAt())
        .visibility(courseCommentDto.getVisibility())
        .userDto(new UserDto(courseCommentDto.getUserId(),
            courseCommentDto.getUserNickName(),
            courseCommentDto.getUserProfileImage())
        )
        .build();
  }
}