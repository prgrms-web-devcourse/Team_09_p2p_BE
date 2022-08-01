package com.prgrms.p2p.domain.place.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.prgrms.p2p.domain.course.dto.CreateCoursePlaceRequest;
import com.prgrms.p2p.domain.place.dto.DetailPlaceResponse;
import com.prgrms.p2p.domain.place.dto.SearchPlaceRequest;
import com.prgrms.p2p.domain.place.dto.SummaryPlaceResponse;
import com.prgrms.p2p.domain.place.entity.Category;
import com.prgrms.p2p.domain.place.entity.PhoneNumber;
import com.prgrms.p2p.domain.place.entity.Place;
import com.prgrms.p2p.domain.place.repository.PlaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
      Category category = Category.CAFE;
      String number = "010-1" + i + "4-1234";
      PhoneNumber phoneNumber = new PhoneNumber(number);

      CreateCoursePlaceRequest request = CreateCoursePlaceRequest.builder()
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
      placeId = placeService.save(request).getId();
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
      Category category = Category.CAFE;
      String number = "010-1234-1234";
      PhoneNumber phoneNumber = new PhoneNumber(number);

      CreateCoursePlaceRequest createPlaceReq = CreateCoursePlaceRequest.builder()
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
      Long placeId = placeService.save(createPlaceReq).getId();

      Place place = placeRepository.findById(placeId)
          .orElseThrow(RuntimeException::new);

      //then
      assertThat(createPlaceReq)
          .usingRecursiveComparison()
          .ignoringFields("addressName", "roadAddressName", "isRecommended", "description")
          .isEqualTo(place);

      assertThat(createPlaceReq.getAddressName()).isEqualTo(place.getAddress().getAddressName());
      assertThat(createPlaceReq.getRoadAddressName()).isEqualTo(
          place.getAddress().getRoadAddressName());
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

  @Nested
  @DisplayName("요약 리스트 조회")
  class findSummaryList {

    @Test
    @DisplayName("성공: 요약 리스트 조회 성공")
    public void findSummaryList() throws Exception {

      //given
      String keyword = "";
      String category = null;

      SearchPlaceRequest searchPlaceReq = SearchPlaceRequest.builder()
          .keyword(keyword)
          .category(category)
          .build();

      Pageable pageable = PageRequest.of(0, 10);

      //when
      Slice<SummaryPlaceResponse> summaryList
          = placeService.findSummaryList(searchPlaceReq, pageable);

      //then
      for (SummaryPlaceResponse summaryPlaceResponse : summaryList) {
        assertThat(summaryPlaceResponse.getTitle()).contains(keyword);
        if (category != null) {
          assertThat(summaryPlaceResponse.getCategory()).isEqualTo(category);
        }
      }
    }
  }
}