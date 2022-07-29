package com.prgrms.p2p.domain.user.dto;

import com.prgrms.p2p.domain.user.entity.Sex;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDetailResponse {

  private Long id;
  private String email;
  private String nickname;
  private String profileImage;
  private String birth;
  private Sex sex;
  private String createdAt;
  private String updatedAt;

}