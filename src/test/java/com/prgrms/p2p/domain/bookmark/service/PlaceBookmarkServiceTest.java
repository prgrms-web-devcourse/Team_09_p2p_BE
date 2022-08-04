package com.prgrms.p2p.domain.bookmark.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.prgrms.p2p.domain.bookmark.entity.PlaceBookmark;
import com.prgrms.p2p.domain.bookmark.repository.PlaceBookmarkRepository;
import com.prgrms.p2p.domain.common.exception.BadRequestException;
import com.prgrms.p2p.domain.common.exception.NotFoundException;
import com.prgrms.p2p.domain.like.entity.PlaceLike;
import com.prgrms.p2p.domain.place.entity.Address;
import com.prgrms.p2p.domain.place.entity.Category;
import com.prgrms.p2p.domain.place.entity.PhoneNumber;
import com.prgrms.p2p.domain.place.entity.Place;
import com.prgrms.p2p.domain.place.repository.PlaceRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@TestClassOrder(ClassOrderer.DisplayName.class)
class PlaceBookmarkServiceTest {

  @Mock
  private PlaceRepository placeRepository;

  @Mock
  private PlaceBookmarkRepository placeBookmarkRepository;

  @InjectMocks
  private PlaceBookmarkService bookmarkService;

  @Nested
  @DisplayName("toggle() : 장소에 북마크를 등록하거나 삭제합니다.")
  @TestMethodOrder(MethodOrderer.DisplayName.class)
  class ToggleTest {

    private Place placeStub;
    private PlaceBookmark placeBookmarkStub;

    @BeforeEach
    void setUp() {
      placeStub = new Place("kakaomapID", "name",
          new Address("addressName", "roadAddressName"),
          "12.123", "123.23", Category.AC5, new PhoneNumber("010-1234-5678"));
      placeBookmarkStub = new PlaceBookmark(1L, placeStub);
    }

    @Test
    @DisplayName("성공 : 존재하는 장소에 북마크가 등록되었습니다.")
    public void successAddition() {
      // Given
      when(placeRepository.findById(any(Long.class))).thenReturn(Optional.of(placeStub));
      when(placeBookmarkRepository.findByUserIdAndPlace(
          any(Long.class), any(Place.class))).thenReturn(Optional.empty());
      when(placeBookmarkRepository.save(any(PlaceBookmark.class))).thenReturn(placeBookmarkStub);
      // When
      bookmarkService.toggle(1L, 1L);
      // Then
      verify(placeRepository, times(1)).findById(any(Long.class));
      verify(placeBookmarkRepository, times(1)).findByUserIdAndPlace(any(Long.class), any(Place.class));
      verify(placeBookmarkRepository, times(1)).save(any(PlaceBookmark.class));
    }

    @Test
    @DisplayName("성공 : 기존에 등록되어 있는 북마크를 삭제합니다.")
    public void successDeletion() {
      // Given
      when(placeRepository.findById(any(Long.class))).thenReturn(Optional.of(placeStub));
      when(placeBookmarkRepository.findByUserIdAndPlace(any(Long.class), any(Place.class)))
          .thenReturn(Optional.of(placeBookmarkStub));
      doNothing().when(placeBookmarkRepository).delete(any(PlaceBookmark.class));
      // When
      bookmarkService.toggle(1L, 1L);
      // Then
      verify(placeRepository, times(1)).findById(any(Long.class));
      verify(placeBookmarkRepository, times(1)).findByUserIdAndPlace(any(Long.class), any(Place.class));
      verify(placeBookmarkRepository, times(1)).delete(any(PlaceBookmark.class));
    }

    @Test
    @DisplayName("실패 : 존재하지 않는 장소에 좋아요가 등록되었습니다.")
    public void failAddition() {
      // Given
      when(placeRepository.findById(any(Long.class))).thenReturn(Optional.empty());
      // When
      Throwable response = catchThrowable(() -> bookmarkService.toggle(1L, 1L));
      // Then
      verify(placeRepository, times(1)).findById(any(Long.class));
      assertThat(response).isInstanceOf(NotFoundException.class);
    }
  }
}