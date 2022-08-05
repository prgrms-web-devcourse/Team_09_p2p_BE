package com.prgrms.p2p.domain.user.dto;

import com.prgrms.p2p.domain.user.entity.Sex;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "내 정보 조회 응답 DTO")
@Getter
@Builder
public class UserDetailResponse {

  @Schema(description = "유저 아이디", example = "1")
  private Long id;

  @Schema(description = "이메일", example = "test@gmail.com")
  private String email;

  @Schema(description = "닉네임", example = "beomsic")
  private String nickname;

  @Schema(description = "프로필 사진", example = "http://image.com")
  private String profileImage;

  @Schema(description = "생년월일", example = "1997-11-29")
  private String birth;

  @Schema(description = "성별", example = "test@gmail.com")
  private Sex sex;

  @Schema(description = "생성 날짜", example = "2022-08-04T09:51:24")
  private String createdAt;

  @Schema(description = "최신 수정 날짜", example = "2022-08-04T09:51:24")
  private String updatedAt;

}
