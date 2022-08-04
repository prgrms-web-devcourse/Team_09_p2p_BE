package com.prgrms.p2p.domain.like.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

import com.prgrms.p2p.domain.common.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LikeServiceFactoryTest {

  @Autowired
  LikeServiceFactory likeServiceFactory;

  @Nested
  @DisplayName("getLikeService() : type에 따른 좋아요 서비스를 반환합니다.")
  class GetLikeServiceTest {

    @Test
    @DisplayName("성공 : courseLikeService 에 해당하는 스프링 빈을 찾아냅니다.")
    void successCourseLikeService() {
      LikeService response = likeServiceFactory.getLikeService("courseLikeService");
      assertThat(response).isInstanceOf(CourseLikeService.class);
    }

    @Test
    @DisplayName("성공 : placeLikeService 에 해당하는 스프링 빈을 찾아냅니다.")
    void successPlaceLikeService() {
      LikeService response = likeServiceFactory.getLikeService("placeLikeService");
      assertThat(response).isInstanceOf(PlaceLikeService.class);
    }

    @Test
    @DisplayName("실패 : 요청에 해당하는 스프링 빈을 찾아내지 못할 경우 예외를 반환합니다.")
    void failLikeService() {
      Throwable response = catchThrowable(
          () -> likeServiceFactory.getLikeService("userLikeService"));
      assertThat(response).isInstanceOf(BadRequestException.class);
    }
  }
}