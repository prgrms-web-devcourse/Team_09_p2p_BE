package com.prgrms.p2p.domain.user.service;

import com.prgrms.p2p.domain.bookmark.service.CourseBookmarkService;
import com.prgrms.p2p.domain.bookmark.service.PlaceBookmarkService;
import com.prgrms.p2p.domain.comment.service.CourseCommentService;
import com.prgrms.p2p.domain.course.service.CourseService;
import com.prgrms.p2p.domain.user.dto.OtherUserDetailResponse;
import com.prgrms.p2p.domain.user.dto.UserBookmarkResponse;
import com.prgrms.p2p.domain.user.dto.UserDetailResponse;
import com.prgrms.p2p.domain.user.entity.User;
import com.prgrms.p2p.domain.user.util.UserConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserFacadeService {

  private final PlaceBookmarkService placeBookmarkService;
  private final CourseBookmarkService courseBookmarkService;
  private final CourseCommentService courseCommentService;
  private final CourseService courseService;


  public UserDetailResponse getInfo(User user) {

    Long userId = user.getId();

    Long courseBookmark = courseBookmarkService.countByUserId(userId);
    Long placeBookmark = placeBookmarkService.countByUserId(userId);
    UserBookmarkResponse userBookmarkResponse = UserConverter.toUserBookmark(placeBookmark, courseBookmark);

    //TODO : 장소 코멘트가 생기면 갯수를 더해 주어야 합니다.
    Long courseComment = courseCommentService.countByUserId(userId);

    Long userCourse = courseService.countByUserId(userId);

    return UserConverter.detailFromUser(
        user,
        userBookmarkResponse,
        courseComment,
        userCourse);
  }

  public OtherUserDetailResponse getOtherInfo(User user) {
    Long userId = user.getId();

    Long courseBookmark = courseBookmarkService.countByUserId(userId);
    Long placeBookmark = placeBookmarkService.countByUserId(userId);
    UserBookmarkResponse userBookmarkResponse = UserConverter.toUserBookmark(placeBookmark, courseBookmark);

    //TODO : 장소 코멘트가 생기면 갯수를 더해 주어야 합니다.
    Long courseComment = courseCommentService.countByUserId(userId);

    Long userCourse = courseService.countByUserId(userId);

    return UserConverter.otherDetailFromUser(
        user,
        userBookmarkResponse,
        courseComment,
        userCourse);
  }
}
