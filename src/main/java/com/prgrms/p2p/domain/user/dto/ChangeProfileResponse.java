package com.prgrms.p2p.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "프로필 사진 교체 요청 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeProfileResponse {

  @Schema(description = "프로필 이미지 S3 url", example = "http://test.com")
  private String profileImage;

}
