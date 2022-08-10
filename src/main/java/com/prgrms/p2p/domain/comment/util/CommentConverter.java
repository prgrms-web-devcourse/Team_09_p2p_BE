package com.prgrms.p2p.domain.comment.util;

import com.prgrms.p2p.domain.comment.dto.CourseCommentForQueryDsl;
import com.prgrms.p2p.domain.comment.dto.CourseCommentDto;
import com.prgrms.p2p.domain.comment.dto.CourseCommentDto.UserDto;
import com.prgrms.p2p.domain.comment.dto.CourseCommentResponse;
import com.prgrms.p2p.domain.comment.dto.CreateCommentRequest;
import com.prgrms.p2p.domain.comment.dto.MergeCommentResponse;
import com.prgrms.p2p.domain.comment.dto.MergeCommentResponse.Content;
import com.prgrms.p2p.domain.comment.dto.PlaceCommentDto;
import com.prgrms.p2p.domain.comment.dto.PlaceCommentForQueryDsl;
import com.prgrms.p2p.domain.comment.dto.PlaceCommentResponse;
import com.prgrms.p2p.domain.comment.entity.CourseComment;
import com.prgrms.p2p.domain.comment.entity.PlaceComment;
import com.prgrms.p2p.domain.comment.entity.Visibility;
import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.place.entity.Place;
import java.util.List;
import java.util.stream.Collectors;

public class CommentConverter {

  public static CourseComment toCourseComment(
      CreateCommentRequest createCourseCommentReq, Course course, Long userId) {

    return new CourseComment(
        createCourseCommentReq.getComment(),
        createCourseCommentReq.getRootCommentId(),
        userId,
        course
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

  public static PlaceComment toPlaceComment(
      CreateCommentRequest commentRequest, Place place, Long userId) {

    return new PlaceComment(commentRequest.getComment(),
        commentRequest.getRootCommentId(),
        userId,
        place
    );
  }

  public static PlaceCommentResponse toPlaceCommentResponse(
      List<PlaceCommentForQueryDsl> commentForQueryDsl, Long placeId
  ) {

    List<PlaceCommentDto> commentDtoList = commentForQueryDsl.stream()
        .map(CommentConverter::toPlaceCommentResponse)
        .collect(Collectors.toList());

    return PlaceCommentResponse.builder()
        .id(placeId)
        .totalCount((long) commentDtoList.size())
        .placeComments(commentDtoList)
        .build();
  }

  public static MergeCommentResponse toMergeCommentResponse(CourseCommentForQueryDsl comment,
      String type) {

    return MergeCommentResponse.builder()
        .id(comment.getId())
        .rootCommentId(comment.getRootCommentId())
        .comment(comment.getComment())
        .createdAt(comment.getCreatedAt())
        .updatedAt(comment.getUpdatedAt())
        .subCommentCount(comment.getSubCommentCount())
        .userId(comment.getUserId())
        .content(new Content(
            comment.getContentId(),
            type,
            comment.getContentTitle())
        )
        .build();
  }

  private static PlaceCommentDto toPlaceCommentResponse(
      PlaceCommentForQueryDsl placeCommentForQueryDsl) {

    if (placeCommentForQueryDsl.getVisibility().equals(Visibility.DELETED_INFORMATION)) {
      placeCommentForQueryDsl.isDeletedComment();
    }

    return PlaceCommentDto.builder()
        .id(placeCommentForQueryDsl.getId())
        .comment(placeCommentForQueryDsl.getComment())
        .rootCommentId(placeCommentForQueryDsl.getRootCommentId())
        .createdAt(placeCommentForQueryDsl.getCreatedAt())
        .updatedAt(placeCommentForQueryDsl.getUpdatedAt())
        .user(
            new PlaceCommentDto.UserDto(
                placeCommentForQueryDsl.getId(),
                placeCommentForQueryDsl.getUserNickName(),
                placeCommentForQueryDsl.getUserProfileImage()
            )
        )
        .build();
  }
}