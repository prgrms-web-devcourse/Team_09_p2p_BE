package com.prgrms.p2p.domain.like.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

import com.prgrms.p2p.domain.place.entity.Address;
import com.prgrms.p2p.domain.place.entity.Category;
import com.prgrms.p2p.domain.place.entity.Place;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.DisplayName.class)
class PlaceLikeTest {

  @Test
  @DisplayName("성공 : 장소 좋아요 객체를 생성합니다.")
  public void success() {
    // Given
    Place place = new Place("1234", "1234", new Address("123","234"), "1234", "1234", Category.AC5, null);
    // When
    PlaceLike placeLike = new PlaceLike(1L, place);
    // Then
    assertThat(place.getPlaceLikes()).contains(placeLike);
  }

  @Test
  @DisplayName("실패 : 유저 아이디가 빈값인 경우 예외를 반환합니다.")
  public void failByUserId1() {
    // Given
    Place place = new Place("1234", "1234", new Address("123","234"), "1234", "1234", Category.AC5, null);
    // When
    Throwable response = catchThrowable(() -> new PlaceLike(null, place));
    // Then
    assertThat(response).isInstanceOf(RuntimeException.class);
  }

  @Test
  @DisplayName("실패 : 유저 아이디가 0 이하인 경우 예외를 반환합니다.")
  public void failByUserId2() {
    // Given
    Place place = new Place("1234", "1234", new Address("123","234"), "1234", "1234", Category.AC5, null);
    // When
    Throwable response = catchThrowable(() -> new PlaceLike(0L, place));
    // Then
    assertThat(response).isInstanceOf(RuntimeException.class);
  }

  @Test
  @DisplayName("실패 : 장소가 빈값인 경우 예외를 반환합니다.")
  public void failByPlace() {
    // When
    Throwable response = catchThrowable(() -> new PlaceLike(1L, null));
    // Then
    assertThat(response).isInstanceOf(RuntimeException.class);
  }
}