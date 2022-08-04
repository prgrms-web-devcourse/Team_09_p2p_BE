package com.prgrms.p2p.domain.bookmark.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;

import com.prgrms.p2p.domain.common.exception.BadRequestException;
import com.prgrms.p2p.domain.like.service.CourseLikeService;
import com.prgrms.p2p.domain.like.service.LikeService;
import com.prgrms.p2p.domain.like.service.LikeServiceFactory;
import com.prgrms.p2p.domain.like.service.PlaceLikeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookmarkServiceFactoryTest {

  @Autowired
  BookmarkServiceFactory bookmarkServiceFactory;

  @Nested
  @DisplayName("getBookmarkService() : type에 따른 북마크 서비스를 반환합니다.")
  class GetBookmarkServiceTest {

    @Test
    @DisplayName("성공 : courseBookmarkService 에 해당하는 스프링 빈을 찾아냅니다.")
    void successCourseBookmarkService() {
      BookmarkService response = bookmarkServiceFactory.getBookmarkService("courseBookmarkService");
      assertThat(response).isInstanceOf(CourseBookmarkService.class);
    }

    @Test
    @DisplayName("성공 : placeBookmarkService 에 해당하는 스프링 빈을 찾아냅니다.")
    void successPlaceBookmarkService() {
      BookmarkService response = bookmarkServiceFactory.getBookmarkService("placeBookmarkService");
      assertThat(response).isInstanceOf(PlaceBookmarkService.class);
    }

    @Test
    @DisplayName("실패 : 요청에 해당하는 스프링 빈을 찾아내지 못할 경우 예외를 반환합니다.")
    void failBookmarkService() {
      Throwable response = catchThrowable(
          () -> bookmarkServiceFactory.getBookmarkService("userBookmarkService"));
      assertThat(response).isInstanceOf(BadRequestException.class);
    }
  }

}