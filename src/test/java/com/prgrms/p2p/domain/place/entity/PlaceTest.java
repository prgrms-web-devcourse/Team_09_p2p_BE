package com.prgrms.p2p.domain.place.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class PlaceTest {

  @Nested
  @DisplayName("place 생성")
  class create {

    @Test
    @DisplayName("성공: 생성 성공")
    public void Test() throws Exception {

      //given
      String kakaoMapId = "kakaoMapId";
      String name = "name";

      String addressName = "addressName";
      String roadAddressName = "roadAddressName";
      Address address = new Address(addressName, roadAddressName);

      String latitude = "latitude";
      String logitude = "logitude";
      Category category = Category.CAFE;

      String number = "010-2345-5678";
      PhoneNumber phoneNumber = new PhoneNumber(number);
      String imageUrl = "imageUrl";

      //when
      Place place = new Place(
          kakaoMapId,
          name,
          address,
          latitude,
          logitude,
          category,
          phoneNumber,
          imageUrl);

      //then
      assertThat(place.getKakaoMapId()).isEqualTo(kakaoMapId);
      assertThat(place.getName()).isEqualTo(name);
      assertThat(place.getLatitude()).isEqualTo(latitude);
      assertThat(place.getLongitude()).isEqualTo(logitude);
      assertThat(place.getCategory()).isEqualTo(category);
      assertThat(place.getImageUrl()).isEqualTo(imageUrl);

      assertThat(place.getAddress())
          .usingRecursiveComparison()
          .isEqualTo(address);

      assertThat(place.getPhoneNumber())
          .usingRecursiveComparison()
          .isEqualTo(phoneNumber);
    }
  }
}