package com.prgrms.p2p.domain.place.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.prgrms.p2p.domain.place.dto.CreatePlaceRequest;
import com.prgrms.p2p.domain.place.dto.DetailPlaceResponse;
import com.prgrms.p2p.domain.place.entity.Place;
import com.prgrms.p2p.domain.place.repository.PlaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class PlaceServiceTest {

  @Autowired
  PlaceService placeService;

  @Autowired
  PlaceRepository placeRepository;

  @BeforeEach
  void setup() {
    for (int i = 0; i < 20; i++) {
      //given
      String kakaoMapId = "kakaoMapId" + i;
      String name = "name" + i;
      String addressName = "addressName" + i;
      String roadAddressName = "roadAddressName" + i;
      String latitude = "latitude" + i;
      String longitude = "longitude" + i;
      String category = "CAFE";
      String phoneNumber = "010-1" + i + "4-1234";

      CreatePlaceRequest request = CreatePlaceRequest.builder()
          .kakaoMapId(kakaoMapId)
          .name(name)
          .addressName(addressName)
          .roadAddressName(roadAddressName)
          .latitude(latitude)
          .longitude(longitude)
          .category(category)
          .phoneNumber(phoneNumber)
          .build();

      //when
      placeId = placeService.save(request);
    }
  }

  Long placeId;

  @Nested
  @DisplayName("저장")
  class save {

    @Test
    @DisplayName("성공: 장소 저장에 성공")
    public void savePlace() throws Exception {

      //given
      String kakaoMapId = "kakaoMapId";
      String name = "name";
      String addressName = "addressName";
      String roadAddressName = "roadAddressName";
      String latitude = "latitude";
      String longitude = "longitude";
      String category = "CAFE";
      String phoneNumber = "010-1234-1234";

      CreatePlaceRequest createPlaceReq = CreatePlaceRequest.builder()
          .kakaoMapId(kakaoMapId)
          .name(name)
          .addressName(addressName)
          .roadAddressName(roadAddressName)
          .latitude(latitude)
          .longitude(longitude)
          .category(category)
          .phoneNumber(phoneNumber)
          .build();

      //when
      Long placeId = placeService.save(createPlaceReq);

      Place place = placeRepository.findById(placeId)
          .orElseThrow(RuntimeException::new);

      //then
      assertThat(createPlaceReq)
          .usingRecursiveComparison()
          .ignoringFields("addressName", "roadAddressName", "phoneNumber", "category")
          .isEqualTo(place);

      assertThat(createPlaceReq.getAddressName()).isEqualTo(place.getAddress().getAddressName());
      assertThat(createPlaceReq.getRoadAddressName()).isEqualTo(place.getAddress().getRoadAddressName());
      assertThat(createPlaceReq.getCategory()).isEqualTo(place.getCategory().toString());
      assertThat(createPlaceReq.getPhoneNumber()).isEqualTo(place.getPhoneNumber().getNumber());
    }
  }

  @Nested
  @DisplayName("상세조회")
  class findDetail {

    @Test
    @DisplayName("성공: 상세조회 성공")
    public void findDetail() throws Exception {

      //given
      Place place = placeRepository.findById(placeId).orElseThrow(RuntimeException::new);

      //when
      DetailPlaceResponse detailPlaceResp = placeService.findDetail(placeId);

      //then
      assertThat(detailPlaceResp)
          .usingRecursiveComparison()
          .ignoringFields("likeCount", "usedCount", "phone", "addressName", "roadAddressName",
              "category")
          .isEqualTo(place);

      assertThat(detailPlaceResp.getPhone()).isEqualTo(place.getPhoneNumber().getNumber());
      assertThat(detailPlaceResp.getAddressName()).isEqualTo(place.getAddress().getAddressName());
      assertThat(detailPlaceResp.getRoadAddressName()).isEqualTo(
          place.getAddress().getRoadAddressName());
      assertThat(detailPlaceResp.getCategory()).isEqualTo(place.getCategory().toString());
    }
  }
}