package com.prgrms.p2p.domain.user.service;

import com.prgrms.p2p.domain.bookmark.service.CourseBookmarkService;
import com.prgrms.p2p.domain.bookmark.service.PlaceBookmarkService;
import com.prgrms.p2p.domain.comment.service.CourseCommentService;
import com.prgrms.p2p.domain.course.service.CourseService;
import com.prgrms.p2p.domain.like.service.CourseLikeService;
import com.prgrms.p2p.domain.like.service.PlaceLikeService;
import com.prgrms.p2p.domain.user.dto.OtherUserDetailResponse;
import com.prgrms.p2p.domain.user.dto.UserBookmarkResponse;
import com.prgrms.p2p.domain.user.dto.UserCommentResponse;
import com.prgrms.p2p.domain.user.dto.UserDetailResponse;
import com.prgrms.p2p.domain.user.dto.UserLikeResponse;
import com.prgrms.p2p.domain.user.entity.User;
import com.prgrms.p2p.domain.user.util.UserConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserFacadeService {

  private final CourseLikeService courseLikeService;
  private final PlaceLikeService placeLikeService;
  private final PlaceBookmarkService placeBookmarkService;
  private final CourseBookmarkService courseBookmarkService;
  private final CourseCommentService courseCommentService;
  private final CourseService courseService;


  public UserDetailResponse getInfo(User user) {

    Long userId = user.getId();

    Long courseLike = courseLikeService.countByUserId(userId);
    Long placeLike = placeLikeService.countByUserId(userId);
    UserLikeResponse userLikeResponse = UserConverter.toUserLike(placeLike, courseLike);

    Long courseBookmark = courseBookmarkService.countByUserId(userId);
    Long placeBookmark = placeBookmarkService.countByUserId(userId);
    UserBookmarkResponse userBookmarkResponse = UserConverter.toUserBookmark(placeBookmark, courseBookmark);

    Long courseComment = courseCommentService.countByUserId(userId);
    //TODO: 장소 댓글 기능 추가시 수정
    UserCommentResponse userCommentResponse = UserConverter.toUserComment(0l, courseComment);

    Long userCourse = courseService.countByUserId(userId);

    return UserConverter.detailFromUser(
        user,
        userLikeResponse,
        userBookmarkResponse,
        userCommentResponse,
        userCourse);
  }

  public OtherUserDetailResponse getOtherInfo(User user) {
    Long userId = user.getId();

    Long courseLike = courseLikeService.countByUserId(userId);
    Long placeLike = placeLikeService.countByUserId(userId);
    UserLikeResponse userLikeResponse = UserConverter.toUserLike(placeLike, courseLike);

    Long courseBookmark = courseBookmarkService.countByUserId(userId);
    Long placeBookmark = placeBookmarkService.countByUserId(userId);
    UserBookmarkResponse userBookmarkResponse = UserConverter.toUserBookmark(placeBookmark, courseBookmark);

    Long courseComment = courseCommentService.countByUserId(userId);
    UserCommentResponse userCommentResponse = UserConverter.toUserComment(0l, courseComment);

    Long userCourse = courseService.countByUserId(userId);

    return UserConverter.otherDetailFromUser(
        user,
        userLikeResponse,
        userBookmarkResponse,
        userCommentResponse,
        userCourse);
  }
}
