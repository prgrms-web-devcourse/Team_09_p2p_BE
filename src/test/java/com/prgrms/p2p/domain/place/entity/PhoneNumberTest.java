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
class PhoneNumberTest {

  @Nested
  @DisplayName("phoneNumber 생성")
  class create {

    @Test
    @DisplayName("성공: 전화번호 생성")
    public void createPhoneNumber() throws Exception {

      //given
      String number_2_3_4 = "02-234-5678";
      String number_3_3_4 = "010-234-5678";
      String number_2_4_4 = "02-2345-5678";
      String number_3_4_4 = "010-2345-5678";

      //when
      PhoneNumber phoneNumber_2_3_4 = new PhoneNumber(number_2_3_4);
      PhoneNumber phoneNumber_3_3_4 = new PhoneNumber(number_3_3_4);
      PhoneNumber phoneNumber_2_4_4 = new PhoneNumber(number_2_4_4);
      PhoneNumber phoneNumber_3_4_4 = new PhoneNumber(number_3_4_4);

      //then
      assertThat(phoneNumber_2_3_4.getNumber()).isEqualTo(number_2_3_4);
      assertThat(phoneNumber_3_3_4.getNumber()).isEqualTo(number_3_3_4);
      assertThat(phoneNumber_2_4_4.getNumber()).isEqualTo(number_2_4_4);
      assertThat(phoneNumber_3_4_4.getNumber()).isEqualTo(number_3_4_4);
    }

    @Test
    @DisplayName("실패: 전화번호 생성")
    public void failByWrongNumber() throws Exception {

      //given
      String number_1_4_4 = "0-2345-5678";
      String number_4_4_4 = "0111-2345-5678";
      String number_2_2_4 = "02-23-5678";
      String number_2_5_4 = "02-23555-5678";
      String number_2_4_3 = "02-2354-567";
      String number_2_4_5 = "02-2354-56789";

      //then
      Assertions.assertThrows(IllegalArgumentException.class, () -> new PhoneNumber(number_1_4_4));
      Assertions.assertThrows(IllegalArgumentException.class, () -> new PhoneNumber(number_4_4_4));
      Assertions.assertThrows(IllegalArgumentException.class, () -> new PhoneNumber(number_2_2_4));
      Assertions.assertThrows(IllegalArgumentException.class, () -> new PhoneNumber(number_2_5_4));
      Assertions.assertThrows(IllegalArgumentException.class, () -> new PhoneNumber(number_2_4_3));
      Assertions.assertThrows(IllegalArgumentException.class, () -> new PhoneNumber(number_2_4_5));
    }

    @Test
    @DisplayName("실패: 전화번호가 공백일 때")
    public void failByBlankNumber() throws Exception {

      //given
      String number = " ";

      //then
      Assertions.assertThrows(IllegalArgumentException.class, () -> new PhoneNumber(number));
    }
  }
}
