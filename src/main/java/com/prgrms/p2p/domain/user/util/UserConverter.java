package com.prgrms.p2p.domain.user.util;

import com.prgrms.p2p.domain.user.dto.LoginResponse;
import com.prgrms.p2p.domain.user.dto.OtherUserDetailResponse;
import com.prgrms.p2p.domain.user.dto.SignUpRequest;
import com.prgrms.p2p.domain.user.dto.UserBookmarkResponse;
import com.prgrms.p2p.domain.user.dto.UserCommentResponse;
import com.prgrms.p2p.domain.user.dto.UserCounts;
import com.prgrms.p2p.domain.user.dto.UserDetailResponse;
import com.prgrms.p2p.domain.user.dto.UserLikeResponse;
import com.prgrms.p2p.domain.user.entity.Authority;
import com.prgrms.p2p.domain.user.entity.User;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserConverter {

  public static User toUser(SignUpRequest signUpRequest) {
    User user = new User(
        signUpRequest.getEmail(),
        PasswordEncrypter.encrypt(signUpRequest.getPassword()),
        signUpRequest.getNickname(),
        signUpRequest.getBirth(),
        signUpRequest.getSex()
    );
    Authority.addUserAuth(user);
    return user;
  }

  public static User toAdmin(SignUpRequest signUpRequest) {
    User user = new User(
        signUpRequest.getEmail(),
        PasswordEncrypter.encrypt(signUpRequest.getPassword()),
        signUpRequest.getNickname(),
        signUpRequest.getBirth(),
        signUpRequest.getSex()
    );
    Authority.addUserAuth(user);
    Authority.addAdminAuth(user);
    return user;
  }

  public static UserDetailResponse detailFromUser(
      User user,
      UserBookmarkResponse userBookmarkResponse,
      Long userComments,
      Long userCourse) {

    String profileUrl = user.getProfileUrl()
        .orElse(null);

    UserDetailResponse userDetail = UserDetailResponse.builder()
        .id(user.getId())
        .email(user.getEmail())
        .nickname(user.getNickname())
        .profileImage(profileUrl)
        .birth(fromLocalDate(user.getBirth()))
        .sex(user.getSex())
        .createdAt(fromLocalDateTime(user.getCreatedAt()))
        .updatedAt(fromLocalDateTime(user.getUpdatedAt()))
        .build();

    UserCounts counts = UserCounts.builder()
        .course(userCourse)
        .comments(userComments)
        .bookmarks(userBookmarkResponse)
        .build();

    userDetail.setCounts(counts);

    return userDetail;
  }


  public static OtherUserDetailResponse otherDetailFromUser(
      User user,
      UserBookmarkResponse userBookmarkResponse,
      Long userComments,
      Long userCourse) {

    String profileUrl = user.getProfileUrl()
        .orElse(null);

    OtherUserDetailResponse otherUserDetail = OtherUserDetailResponse.builder()
        .id(user.getId())
        .email(user.getEmail())
        .nickname(user.getNickname())
        .profileImage(profileUrl)
        .birth(fromLocalDate(user.getBirth()))
        .sex(user.getSex())
        .createdAt(fromLocalDateTime(user.getCreatedAt()))
        .build();

    UserCounts counts = UserCounts.builder()
        .course(userCourse)
        .comments(userComments)
        .bookmarks(userBookmarkResponse)
        .build();

    otherUserDetail.setCounts(counts);

    return otherUserDetail;
  }

  public static LoginResponse fromUserAndToken(User user, String token) {
    String profileUrl = user.getProfileUrl()
        .orElse(null);

    LoginResponse response = LoginResponse.builder()
        .accessToken(token)
        .build();

    LoginResponse.Datas data = response.new Datas(
        user.getId(),
        user.getNickname(),
        profileUrl
    );
    response.setUser(data);
    return response;
  }

  public static UserLikeResponse toUserLike(Long placeLike, Long courseLike) {
    return UserLikeResponse.builder()
        .total(placeLike + courseLike)
        .placeLike(placeLike)
        .courseLike(courseLike).build();
  }

  public static UserCommentResponse toUserComment(Long placeComment, Long courseComment) {
    return UserCommentResponse.builder()
        .total(placeComment + courseComment)
        .placeComment(placeComment)
        .courseComment(courseComment)
        .build();
  }

  public static UserBookmarkResponse toUserBookmark(Long placeBookmark, Long courseBookmark) {
    return UserBookmarkResponse.builder()
        .total(placeBookmark + courseBookmark)
        .placeBookmark(placeBookmark)
        .courseBookmark(courseBookmark)
        .build();
  }

  private static String fromLocalDate(LocalDate date) {
    return String.valueOf(date);
  }

  private static String fromLocalDateTime(LocalDateTime date) {
    return String.valueOf(date);
  }
}
