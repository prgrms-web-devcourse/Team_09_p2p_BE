package com.prgrms.p2p.domain.place.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
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
    public void createPlace() throws Exception {

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

    @Test
    @DisplayName("실패: 카카오 맵 아이디가 공백일 때")
    public void failAsKakaoMapIdIsBlank() throws Exception {

      //given
      String kakaoMapId = " ";
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

      //then
      Assertions.assertThrows(IllegalArgumentException.class,
          () -> new Place(
              kakaoMapId,
              name,
              address,
              latitude,
              logitude,
              category,
              phoneNumber,
              imageUrl));
    }

    @Test
    @DisplayName("실패: 이름이 공백일 때")
    public void failAsNameIsBlank() throws Exception {

      //given
      String kakaoMapId = "kakaoMapId";
      String name = " ";

      String addressName = "addressName";
      String roadAddressName = "roadAddressName";
      Address address = new Address(addressName, roadAddressName);

      String latitude = "latitude";
      String logitude = "logitude";
      Category category = Category.CAFE;

      String number = "010-2345-5678";
      PhoneNumber phoneNumber = new PhoneNumber(number);
      String imageUrl = "imageUrl";

      //then
      Assertions.assertThrows(IllegalArgumentException.class,
          () -> new Place(
              kakaoMapId,
              name,
              address,
              latitude,
              logitude,
              category,
              phoneNumber,
              imageUrl));
    }

    @Test
    @DisplayName("실패: 위도가 공백일 때")
    public void failAsLatitudeIsBlank() throws Exception {

      //given
      String kakaoMapId = "kakaoMapId";
      String name = "name";

      String addressName = "addressName";
      String roadAddressName = "roadAddressName";
      Address address = new Address(addressName, roadAddressName);

      String latitude = " ";
      String logitude = "logitude";
      Category category = Category.CAFE;

      String number = "010-2345-5678";
      PhoneNumber phoneNumber = new PhoneNumber(number);
      String imageUrl = "imageUrl";

      //then
      Assertions.assertThrows(IllegalArgumentException.class,
          () -> new Place(
              kakaoMapId,
              name,
              address,
              latitude,
              logitude,
              category,
              phoneNumber,
              imageUrl));
    }

    @Test
    @DisplayName("실패: 경도가 공백일 때")
    public void failAsLogitudeIsBlank() throws Exception {

      //given
      String kakaoMapId = "kakaoMapId";
      String name = "name";

      String addressName = "addressName";
      String roadAddressName = "roadAddressName";
      Address address = new Address(addressName, roadAddressName);

      String latitude = "latitude";
      String logitude = " ";
      Category category = Category.CAFE;

      String number = "010-2345-5678";
      PhoneNumber phoneNumber = new PhoneNumber(number);
      String imageUrl = "imageUrl";

      //then
      Assertions.assertThrows(IllegalArgumentException.class,
          () -> new Place(
              kakaoMapId,
              name,
              address,
              latitude,
              logitude,
              category,
              phoneNumber,
              imageUrl));
    }

    @Test
    @DisplayName("성공: category 없이 생성 성공")
    public void createPlaceWithoutCategory() throws Exception {

      //given
      String kakaoMapId = "kakaoMapId";
      String name = "name";

      String addressName = "addressName";
      String roadAddressName = "roadAddressName";
      Address address = new Address(addressName, roadAddressName);

      String latitude = "latitude";
      String logitude = "logitude";
      Category category = null;

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
      assertThat(place.getCategory()).isNull();
      assertThat(place.getImageUrl()).isEqualTo(imageUrl);

      assertThat(place.getAddress())
          .usingRecursiveComparison()
          .isEqualTo(address);

      assertThat(place.getPhoneNumber())
          .usingRecursiveComparison()
          .isEqualTo(phoneNumber);
    }

    @Test
    @DisplayName("성공: 이미지 없이 생성 성공")
    public void createPlaceWihtoutImage() throws Exception {

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
      String imageUrl = null;

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
      assertThat(place.getImageUrl()).isNull();

      assertThat(place.getAddress())
          .usingRecursiveComparison()
          .isEqualTo(address);

      assertThat(place.getPhoneNumber())
          .usingRecursiveComparison()
          .isEqualTo(phoneNumber);
    }
  }
}