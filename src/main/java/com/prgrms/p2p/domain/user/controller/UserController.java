package com.prgrms.p2p.domain.user.controller;

import com.prgrms.p2p.domain.common.service.UploadServiceImpl;
import com.prgrms.p2p.domain.user.aop.annotation.Auth;
import com.prgrms.p2p.domain.user.aop.annotation.CurrentUser;
import com.prgrms.p2p.domain.user.dto.ChangePasswordRequest;
import com.prgrms.p2p.domain.user.dto.ChangeProfileResponse;
import com.prgrms.p2p.domain.user.dto.LoginFailResponse;
import com.prgrms.p2p.domain.user.dto.LoginRequest;
import com.prgrms.p2p.domain.user.dto.LoginResponse;
import com.prgrms.p2p.domain.user.dto.ModifyRequest;
import com.prgrms.p2p.domain.user.dto.OtherUserDetailResponse;
import com.prgrms.p2p.domain.user.dto.SignUpRequest;
import com.prgrms.p2p.domain.user.dto.SignUpResponse;
import com.prgrms.p2p.domain.user.dto.UserDetailResponse;
import com.prgrms.p2p.domain.user.pojo.CustomUserDetails;
import com.prgrms.p2p.domain.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import java.net.URI;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin("*")
@Api(tags = {"유저에 대한 controller"})
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

  private final UserService userService;
  private final UploadServiceImpl uploadService;

  public UserController(UserService userService,
      UploadServiceImpl uploadService) {
    this.userService = userService;
    this.uploadService = uploadService;
  }

  @Operation(summary = "회원가입 기능", description = "회원가입을 위한 정보를 입력 후 회원가입 요청을 합니다.")
  @ApiResponses(value = {
      @ApiResponse(code = 201, message = "회원가입 성공", response = SignUpResponse.class),
      @ApiResponse(code = 400, message = "회원가입 실패")
  })
  @PostMapping()
  public ResponseEntity<SignUpResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
    String nickname = userService.signUp(signUpRequest);
    return ResponseEntity.created(URI.create("/")).body(new SignUpResponse(nickname));
  }

  @Operation(summary = "로그인 기능", description = "이메일과 비밀번호를 통해 로그인을 할 수 있습니다.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "로그인 성공", response = LoginResponse.class),
      @ApiResponse(code = 401, message = "로그인 실패", response = LoginFailResponse.class)
  })
  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

    //TODO: UnAuthorizedAccessException 만들기
    LoginResponse login = userService.login(loginRequest.getEmail(), loginRequest.getPassword())
        .orElseThrow(RuntimeException::new);

    return ResponseEntity.ok(login);
  }

  @Operation(summary = "이메일 중복 체크", description = "회원가입 전 중복되는 이메일이 있는지 확인합니다.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "이메일 중복 없음"),
      @ApiResponse(code = 409, message = "이미 해당 이메일은 등록되어 있음")
  })
  @PostMapping("/email")
  public void validateEmail(@RequestBody Map<String,String> emailMap) {
    userService.validateEmail(emailMap.get("email"));
  }

  @Operation(summary = "닉네임 중복 체크", description = "회원가입 전 중복되는 닉네임이 있는지 확인합니다.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "닉네임 중복 없음"),
      @ApiResponse(code = 409, message = "이미 해당 닉네임은 등록되어 있음")
  })
  @PostMapping("/nickname")
  public void validateNickname(
      @ApiParam(value = "중복체크를 할 닉네임")
      @RequestBody Map<String,String> nicknameMap) {
    userService.validateNickname(nicknameMap.get("nickname"));
  }


  @Auth
  @Operation(summary = "내 정보 조회", description = "로그인한 유저가 토큰을 이용해 자신의 정보를 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "정보 조회 성공", response = UserDetailResponse.class),
      @ApiResponse(code = 401, message = "토큰 인증 실패")
  })
  @GetMapping()
  public ResponseEntity<UserDetailResponse> getUserInfo(@CurrentUser CustomUserDetails user) {
    UserDetailResponse userInfo = userService.getUserInfo(user.getId());
    return ResponseEntity.ok(userInfo);
  }

  @Operation(summary = "타인의 정보 조회", description = "타인의 유저 아이디를 이용해 타인의 정보를 조회할 수 있습니다.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "타인 정보 조회 성공", response = OtherUserDetailResponse.class),
      @ApiResponse(code = 404, message = "해당 아이디를 가진 유저가 없습니다.")
  })
  @GetMapping("/{userId}")
  public ResponseEntity<OtherUserDetailResponse> getOtherInfo(
      @ApiParam(value = "타인의 유저 아이디")
      @PathVariable Long userId) {
    OtherUserDetailResponse userInfo = userService.getOtherInfo(userId);
    return ResponseEntity.ok(userInfo);
  }

  @Auth
  @Operation(summary = "자신의 정보 수정", description = "로그인한 유저가 자신의 정보를 수정할 수 있습니다.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "정보 수정 성공"),
      @ApiResponse(code = 400, message = "수정할 정보가 잘못 입력되었습니다."),
      @ApiResponse(code = 401, message = "토큰 인증 실패")
  })
  @PutMapping()
  public ResponseEntity modify(@RequestBody ModifyRequest modifyRequest, @CurrentUser CustomUserDetails user) {
    userService.modify(user.getId(),
        modifyRequest.getNickname(),
        modifyRequest.getBirth(),
        modifyRequest.getSex());

    return ResponseEntity.ok().build();
  }

  @Auth
  @Operation(summary = "비밀번호 수정", description = "로그인한 유저가 자신의 비밀번호를 수정할 수 있습니다.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "변경 성공"),
      @ApiResponse(code = 400, message = "잘못된 비밀번호 입력"),
      @ApiResponse(code = 401, message = "인증 실패"),
      @ApiResponse(code = 409, message = "원래 있던 패스워드와 같은 경우")
  })
  @PutMapping("/password")
  public ResponseEntity changePassword(@RequestBody ChangePasswordRequest changePasswordRequest, @CurrentUser CustomUserDetails user){
    //TODO: 유저 아이디를 나중에 어노테이션으로 가져올 예정
    userService.changePassword(user.getId(), changePasswordRequest.getOldPassword(), changePasswordRequest.getNewPassword());

    return ResponseEntity.ok().build();
  }

  @Auth
  @Operation(summary = "프로필 사진 수정", description = "로그인한 유저가 자신의 프로필 사진을 수정할 수 있습니다.")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "Content-Type", example = "multipart/form-data", paramType = "header"),
  })
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "변경 성공"),
      @ApiResponse(code = 401, message = "인증 실패"),
      @ApiResponse(code = 500, message = "s3 업로드 실패")
  })
  @PutMapping("/profile")
  public ResponseEntity<ChangeProfileResponse> changeProfileImage(@CurrentUser CustomUserDetails user, @RequestParam() MultipartFile file) {
    String profileUrl = uploadService.uploadImg(file);
    ChangeProfileResponse changeProfileResponse = userService.changeProfileUrl(user.getId(), profileUrl);
    return ResponseEntity.ok(changeProfileResponse);
  }

  @Auth
  @Operation(summary = "회원 탈퇴", description = "회원가입을 했던 유저는 회원탈퇴를 할 수 있습니다.")
  @ApiResponses(value = {
      @ApiResponse(code = 204, message = "회원 탈퇴 성공"),
      @ApiResponse(code = 401, message = "인증 실패")
  })
  @DeleteMapping()
  public ResponseEntity delete(@CurrentUser CustomUserDetails user){
    //TODO: 유저 아이디를 나중에 어노테이션으로 가져올 예정
    userService.delete(user.getId());

    return ResponseEntity.noContent().build();
  }
}
