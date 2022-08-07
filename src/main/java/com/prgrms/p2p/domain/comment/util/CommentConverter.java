package com.prgrms.p2p.domain.comment.util;

import com.prgrms.p2p.domain.comment.dto.CourseCommentForQueryDsl;
import com.prgrms.p2p.domain.comment.dto.CourseCommentDto;
import com.prgrms.p2p.domain.comment.dto.CourseCommentDto.UserDto;
import com.prgrms.p2p.domain.comment.dto.CourseCommentResponse;
import com.prgrms.p2p.domain.comment.dto.CreateCommentRequest;
import com.prgrms.p2p.domain.comment.entity.CourseComment;
import com.prgrms.p2p.domain.comment.entity.Visibility;
import com.prgrms.p2p.domain.course.entity.Course;
import java.util.List;
import java.util.stream.Collectors;

public class CommentConverter {

  public static CourseComment toCourseComment(
      CreateCommentRequest createCourseCommentReq, Course course, Long seq, Long userId) {

    return new CourseComment(
        createCourseCommentReq.getComment(),
        createCourseCommentReq.getRootCommentId(),
        userId,
        course,
        seq
    );
  }

  public static CourseCommentResponse toCourseCommentResponse(
      List<CourseCommentForQueryDsl> commentForQueryDsl, Long courseId
  ) {

    List<CourseCommentDto> commentDtoList = commentForQueryDsl.stream()
        .map(CommentConverter::toCourseCommentResponse)
        .collect(Collectors.toList());

    return CourseCommentResponse.builder()
        .id(courseId)
        .totalCount((long) commentDtoList.size())
        .courseComments(commentDtoList)
        .build();
  }

  private static CourseCommentDto toCourseCommentResponse(
      CourseCommentForQueryDsl courseCommentForQueryDsl) {

    if (courseCommentForQueryDsl.getVisibility().equals(Visibility.DELETED_INFORMATION)) {
      courseCommentForQueryDsl.isDeletedComment();
    }

    return CourseCommentDto.builder()
        .id(courseCommentForQueryDsl.getId())
        .comment(courseCommentForQueryDsl.getComment())
        .rootCommentId(courseCommentForQueryDsl.getRootCommentId())
        .createdAt(courseCommentForQueryDsl.getCreatedAt())
        .updatedAt(courseCommentForQueryDsl.getUpdatedAt())
        .user(
            new UserDto(courseCommentForQueryDsl.getUserId(),
                courseCommentForQueryDsl.getUserNickName(),
                courseCommentForQueryDsl.getUserProfileImage())
        )
        .build();
  }
}