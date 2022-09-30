package com.prgrms.p2p.domain.user.dto;

import com.prgrms.p2p.domain.user.entity.Sex;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "타인 정보 조회 응답 DTO")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OtherUserDetailResponse {

  @Schema(description = "타인의 아이디", example = "1")
  private Long id;

  @Schema(description = "타인의 이메일", example = "qjatjr29@khu.ac.kr")
  private String email;

  @Schema(description = "타인의 닉네임", example = "beomsic")
  private String nickname;

  @Schema(description = "타인의 프로필 이미지", example = "http://image.com")
  private String profileImage;

  @Schema(description = "타인의 생년월일", example = "1997-11-29")
  private String birth;

  @Schema(description = "타인의 성별", example = "male")
  private Sex sex;

  @Schema(description = "타인 회원가입 날짜", example = "2022-08-04T09:51:24")
  private String createdAt;

  private UserCounts counts;

  private String grade;

  private int score;
}
