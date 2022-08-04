package com.prgrms.p2p.domain.bookmark.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.prgrms.p2p.domain.bookmark.entity.PlaceBookmark;
import com.prgrms.p2p.domain.place.entity.Address;
import com.prgrms.p2p.domain.place.entity.Category;
import com.prgrms.p2p.domain.place.entity.PhoneNumber;
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
@DisplayName("PlaceBookmarkRepository의 기본적인 동작을 확인합니다.")
@TestMethodOrder(MethodOrderer.DisplayName.class)
class PlaceBookmarkRepositoryTest {

  @Autowired
  private PlaceRepository placeRepository;

  @Autowired
  private PlaceBookmarkRepository placeBookmarkRepository;

  private Place place;
  private PlaceBookmark placeBookmark;

  @BeforeEach
  void setUp() {
    placeBookmarkRepository.deleteAll();
    placeRepository.deleteAll();
    place = new Place("1234", "1234", new Address("address1", "address2"), "1234", "1234",
        Category.AC5, new PhoneNumber("010-1234-5678"));
    placeBookmark = new PlaceBookmark(1L, place);
  }

  @Test
  @DisplayName("save()가 동작하는지 확인합니다.")
  void saveTest() {
    // Given
    placeRepository.save(place);
    // When
    PlaceBookmark expectedPlaceBookmark = placeBookmarkRepository.save(placeBookmark);
    Optional<PlaceBookmark> actualPlaceLike = placeBookmarkRepository.findById(
        placeBookmark.getId());
    // Then
    assertThat(actualPlaceLike).hasValue(expectedPlaceBookmark);
  }

  @Test
  @DisplayName("delete()가 동작하는지 확인합니다.")
  void deleteTest() {
    // Given
    placeRepository.save(place);
    placeBookmarkRepository.save(placeBookmark);
    List<PlaceBookmark> expectedPlaceBookmark = placeBookmarkRepository.findAll();
    assertThat(expectedPlaceBookmark).contains(placeBookmark);
    // When
    placeBookmark.deletePlace(placeBookmark.getPlace());
    placeBookmarkRepository.delete(placeBookmark);
    // Then
    List<PlaceBookmark> actualPlaceBookmark = placeBookmarkRepository.findAll();
    assertThat(actualPlaceBookmark.size()).isZero();
  }

  @Test
  @DisplayName("findByUserIdAndPlace()가 동작하는지 확인합니다.")
  void findByUserIdAndPlaceTest() {
    // Given
    placeRepository.save(place);
    placeBookmarkRepository.save(placeBookmark);
    // When
    Optional<PlaceBookmark> actualPlaceBookmark = placeBookmarkRepository.findByUserIdAndPlace(
        placeBookmark.getUserId(), place);
    // Then
    assertThat(actualPlaceBookmark).hasValue(placeBookmark);
  }
}