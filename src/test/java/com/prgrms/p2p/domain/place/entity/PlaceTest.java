package com.prgrms.p2p.domain.place.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class PlaceTest {

  @BeforeEach
  void setup() {

    //given
    kakaoMapId = "kakaoMapId";
    name = "name";

    addressName = "addressName";
    roadAddressName = "roadAddressName";
    address = new Address(addressName, roadAddressName);

    latitude = "latitude";
    logitude = "logitude";
    category = Category.MT1;

    number = "010-2345-5678";
    phoneNumber = new PhoneNumber(number);
    imageUrl = null;

    //when
    place = new Place(
        kakaoMapId,
        name,
        address,
        latitude,
        logitude,
        category,
        phoneNumber,
        imageUrl);
  }

  String kakaoMapId;
  String name;
  String addressName;
  String roadAddressName;
  Address address;
  String latitude;
  String logitude;
  Category category;
  String number;
  PhoneNumber phoneNumber;
  String imageUrl;
  Place place;

  @Nested
  @DisplayName("place 생성")
  class create {

    @Test
    @DisplayName("성공: 생성 성공")
    public void createPlace() throws Exception {

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
      String name = " ";

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
    @DisplayName("실패: 주소가 null일 때")
    public void failAsAddressIsNull() throws Exception {

      //given
      Address address = null;

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
      String latitude = " ";

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
      String logitude = " ";

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
      Category category = null;

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
    @DisplayName("성공: 전화번호 없이 생성 성공")
    public void createPlaceWithoutPhoneNumber() throws Exception {

      //given
      PhoneNumber phoneNumber = null;

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
    @DisplayName("성공: 이미지 없이 생성 성공")
    public void createPlaceWihtoutImage() throws Exception {

      //given
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

  @Nested
  @DisplayName("place 변경 로직 테스트")
  class change {

    @Test
    @DisplayName("성공: 이름 변경")
    public void changeName() throws Exception {

      //given
      String newName = "newName";

      //when
      place.changeName(newName);

      //then
      assertThat(place.getName()).isEqualTo(newName);
    }

    @Test
    @DisplayName("실패: 이름을 공백으로 변경")
    public void failAsNameIsBlank() throws Exception {

      //given
      String newName = " ";

      //then
      Assertions.assertThrows(IllegalArgumentException.class,
          () -> place.changeName(newName));
    }

    @Test
    @DisplayName("성공: 주소 변경")
    public void changeAddress() throws Exception {

      //given
      Address newAddress = new Address("new", "address");

      //when
      place.changeAddress(newAddress);

      //then
      assertThat(place.getAddress()).isEqualTo(newAddress);
    }

    @Test
    @DisplayName("실패: 변경할 주소가 null")
    public void failAsAddressIsNull() throws Exception {

      //given
      Address newAddress = null;

      //then
      Assertions.assertThrows(IllegalArgumentException.class,
          () -> place.changeAddress(newAddress));
    }

    @Test
    @DisplayName("성공: 카테고리 변경")
    public void changeCategory() throws Exception {

      //given
      Category newCategory = Category.MT1;

      //when
      place.changeCategory(newCategory);

      //then
      assertThat(place.getCategory()).isEqualTo(newCategory);
    }

    @Test
    @DisplayName("성공: 카테고리를 null로 변경")
    public void changeCategoryToNull() throws Exception {

      //given
      Category newCategory = null;

      //when
      place.changeCategory(newCategory);

      //then
      assertThat(place.getCategory()).isNull();
    }

    @Test
    @DisplayName("성공: 전화번호 변경")
    public void changePhoneNumber() throws Exception {

      //given
      PhoneNumber newPhoneNumber = new PhoneNumber("011-1234-4321");

      //when
      place.changePhoneNumber(newPhoneNumber);

      //then
      assertThat(place.getPhoneNumber()).isEqualTo(newPhoneNumber);
    }

    @Test
    @DisplayName("성공: 전화번호를 null로 변경")
    public void changePhoneNumberToNull() throws Exception {

      //given
      PhoneNumber newPhoneNumber = null;

      //when
      place.changePhoneNumber(newPhoneNumber);

      //then
      assertThat(place.getPhoneNumber()).isNull();
    }

    @Test
    @DisplayName("성공: 대표 이미지 변경")
    public void changeImageUrl() throws Exception {

      //given
      String newImageUrl = "/new/Image/Url";

      //when
      place.changeImageUrl(newImageUrl);

      //then
      assertThat(place.getImageUrl()).isEqualTo(newImageUrl);
    }

    @Test
    @DisplayName("성공: 대표 이미지를 null로 변경")
    public void changeImageUrlToNull() throws Exception {

      //given
      String newImageUrl =  null;

      //when
      place.changeImageUrl(newImageUrl);

      //then
      assertThat(place.getImageUrl()).isNull();
    }


  }
}