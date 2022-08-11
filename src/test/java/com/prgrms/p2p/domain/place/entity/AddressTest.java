package com.prgrms.p2p.domain.place.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class AddressTest {

  @Nested
  @DisplayName("Address 생성 테스트")
  class create {

    @Test
    @DisplayName("성공: addressName, roadAddressName 두개 존재")
    public void createAddressAll() throws Exception {

      //given
      String addressName = "addressName";
      String roadAddressName = "roadAddressName";

      //when
      Address address = new Address(addressName, roadAddressName);

      //then
      assertThat(address.getAddressName()).isEqualTo(addressName);
      assertThat(address.getRoadAddressName()).isEqualTo(roadAddressName);
    }

    @Test
    @DisplayName("성공: addressName, roadAddressName 둘중 하나는 존재")
    public void createAddressOnlyOne() throws Exception {

      //given
      String addressName = "addressName";
      String roadAddressName = "roadAddressName";

      //when
      Address addressOnlyAddressName = new Address(addressName, null);
      Address addressOnlyRoadAddressName = new Address(null, roadAddressName);

      //then
      assertThat(addressOnlyAddressName.getAddressName()).isEqualTo(addressName);
      assertThat(addressOnlyAddressName.getRoadAddressName()).isNull();

      assertThat(addressOnlyRoadAddressName.getAddressName()).isNull();
      assertThat(addressOnlyRoadAddressName.getRoadAddressName()).isEqualTo(roadAddressName);
    }

    @Test
    @DisplayName("성공: addressName, roadAddressName 둘다 없어도 생성가능")
    public void failAsNoAddress() throws Exception {

      //given
      String addressName = null;
      String roadAddressName = null;

      //when
      Address addressOnlyAddressName = new Address(addressName, null);
      Address addressOnlyRoadAddressName = new Address(null, roadAddressName);

      //then
      assertThat(addressOnlyAddressName.getAddressName()).isEqualTo(addressName);
      assertThat(addressOnlyAddressName.getRoadAddressName()).isNull();

      assertThat(addressOnlyRoadAddressName.getAddressName()).isNull();
      assertThat(addressOnlyRoadAddressName.getRoadAddressName()).isEqualTo(roadAddressName);
    }
  }
}