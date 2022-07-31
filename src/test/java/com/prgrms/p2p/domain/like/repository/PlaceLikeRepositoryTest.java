package com.prgrms.p2p.domain.like.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.prgrms.p2p.domain.like.entity.PlaceLike;
import com.prgrms.p2p.domain.place.entity.Place;
import com.prgrms.p2p.domain.place.repository.PlaceRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@DisplayName("PlaceLikeRepository의 기본적인 동작을 확인합니다.")
@TestMethodOrder(MethodOrderer.DisplayName.class)
class PlaceLikeRepositoryTest {

  @Autowired
  private PlaceLikeRepository placeLikeRepository;

  @Autowired
  private PlaceRepository placeRepository;

  private Place place;
  private PlaceLike placeLike;

  @BeforeEach
  void setUp() {
    placeLikeRepository.deleteAll();
    placeRepository.deleteAll();
    place = new Place("1234", "1234", null, "1234", "1234", null, null, null);
    placeLike = new PlaceLike(1L, place);
  }

  @Test
  @DisplayName("save()가 동작하는지 확인합니다.")
  void saveTest() {
    // Given
    placeRepository.save(place);
    // When
    PlaceLike expectedPlaceLike = placeLikeRepository.save(placeLike);
    Optional<PlaceLike> actualPlaceLike = placeLikeRepository.findById(placeLike.getId());
    // Then
    assertThat(actualPlaceLike).hasValue(expectedPlaceLike);
  }

  @Test
  @DisplayName("delete()가 동작하는지 확인합니다.")
  void deleteTest() {
    // Given
    placeRepository.save(place);
    placeLikeRepository.save(placeLike);
    List<PlaceLike> expectedPlaceLikes = placeLikeRepository.findAll();
    assertThat(expectedPlaceLikes).contains(placeLike);
    // When
    placeLike.deletePlace(placeLike.getPlace());
    placeLikeRepository.delete(placeLike);
    // Then
    List<PlaceLike> actualPlaceLikes = placeLikeRepository.findAll();
    assertThat(actualPlaceLikes.size()).isZero();
  }

  @Test
  @DisplayName("findByUserIdAndPlace()가 동작하는지 확인합니다.")
  void findByUserIdAndPlaceTest() {
    // Given
    placeRepository.save(place);
    placeLikeRepository.save(placeLike);
    // When
    Optional<PlaceLike> actualPlaceLike = placeLikeRepository.findByUserIdAndPlace(placeLike.getUserId(), place);
    // Then
    assertThat(actualPlaceLike).hasValue(placeLike);
  }
}