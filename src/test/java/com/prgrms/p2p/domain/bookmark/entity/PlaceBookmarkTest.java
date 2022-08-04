package com.prgrms.p2p.domain.bookmark.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

import com.prgrms.p2p.domain.common.exception.BadRequestException;
import com.prgrms.p2p.domain.like.entity.PlaceLike;
import com.prgrms.p2p.domain.place.entity.Address;
import com.prgrms.p2p.domain.place.entity.Category;
import com.prgrms.p2p.domain.place.entity.Place;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.DisplayName.class)
class PlaceBookmarkTest {

  @Test
  @DisplayName("성공 : 장소 북마크 객체를 생성합니다.")
  public void success() {
    // Given
    Place place = new Place("1234", "1234", new Address("123","234"), "1234", "1234", Category.AC5, null);
    // When
    PlaceBookmark placeBookmark = new PlaceBookmark(1L, place);
    // Then
    assertThat(place.getPlaceBookmarks()).contains(placeBookmark);
  }

  @Test
  @DisplayName("실패 : 유저 아이디가 빈값인 경우 예외를 반환합니다.")
  public void failByUserId1() {
    // Given
    Place place = new Place("1234", "1234", new Address("123","234"), "1234", "1234", Category.AC5, null);
    // When
    Throwable response = catchThrowable(() -> new PlaceBookmark(null, place));
    // Then
    assertThat(response).isInstanceOf(BadRequestException.class);
  }

  @Test
  @DisplayName("실패 : 유저 아이디가 0 이하인 경우 예외를 반환합니다.")
  public void failByUserId2() {
    // Given
    Place place = new Place("1234", "1234", new Address("123","234"), "1234", "1234", Category.AC5, null);
    // When
    Throwable response = catchThrowable(() -> new PlaceBookmark(0L, place));
    // Then
    assertThat(response).isInstanceOf(BadRequestException.class);
  }

  @Test
  @DisplayName("실패 : 장소가 빈값인 경우 예외를 반환합니다.")
  public void failByPlace() {
    // When
    Throwable response = catchThrowable(() -> new PlaceBookmark(1L, null));
    // Then
    assertThat(response).isInstanceOf(BadRequestException.class);
  }
}