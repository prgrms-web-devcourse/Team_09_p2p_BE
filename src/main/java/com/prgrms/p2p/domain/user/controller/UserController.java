package com.prgrms.p2p.domain.user.controller;

import com.prgrms.p2p.domain.common.service.UploadServiceImpl;
import com.prgrms.p2p.domain.user.aop.annotation.Auth;
import com.prgrms.p2p.domain.user.aop.annotation.CurrentUser;
import com.prgrms.p2p.domain.user.dto.ChangePasswordRequest;
import com.prgrms.p2p.domain.user.dto.ChangeProfileResponse;
import com.prgrms.p2p.domain.user.dto.LoginRequest;
import com.prgrms.p2p.domain.user.dto.LoginResponse;
import com.prgrms.p2p.domain.user.dto.ModifyRequest;
import com.prgrms.p2p.domain.user.dto.OtherUserDetailResponse;
import com.prgrms.p2p.domain.user.dto.SignUpRequest;
import com.prgrms.p2p.domain.user.dto.SignUpResponse;
import com.prgrms.p2p.domain.user.dto.UserDetailResponse;
import com.prgrms.p2p.domain.user.pojo.CustomUserDetails;
import com.prgrms.p2p.domain.user.service.UserService;
import java.net.URI;
import java.util.Map;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
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

  @PostMapping("")
  public ResponseEntity<SignUpResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
    String nickname = userService.signUp(signUpRequest);
    return ResponseEntity.created(URI.create("/")).body(new SignUpResponse(nickname));
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

    //TODO: UnAuthorizedAccessException 만들기
    LoginResponse login = userService.login(loginRequest.getEmail(), loginRequest.getPassword())
        .orElseThrow(RuntimeException::new);

    return ResponseEntity.ok(login);
  }

  @PostMapping("/email")
  public void validateEmail(@RequestBody Map<String,String> emailMap) {
    userService.validateEmail(emailMap.get("email"));
  }

  @PostMapping("/nickname")
  public void validateNickname(@RequestBody Map<String,String> nicknameMap) {
    userService.validateNickname(nicknameMap.get("nickname"));
  }

  @Auth
  @GetMapping("/")
  public ResponseEntity<UserDetailResponse> getUserInfo(@CurrentUser CustomUserDetails user) {
    UserDetailResponse userInfo = userService.getUserInfo(user.getId());
    return ResponseEntity.ok(userInfo);
  }

  @GetMapping("/{userId}")
  public ResponseEntity<OtherUserDetailResponse> getOtherInfo(@PathVariable Long userId) {
    OtherUserDetailResponse userInfo = userService.getOtherInfo(userId);
    return ResponseEntity.ok(userInfo);
  }

  @Auth
  @PutMapping("")
  public ResponseEntity modify(@RequestBody ModifyRequest modifyRequest, @CurrentUser CustomUserDetails user) {
    userService.modify(user.getId(),
        modifyRequest.getNickname(),
        modifyRequest.getBirth(),
        modifyRequest.getSex());

    return ResponseEntity.ok().build();
  }

  @Auth
  @PutMapping("/password")
  public ResponseEntity changePassword(@RequestBody ChangePasswordRequest changePasswordRequest, @CurrentUser CustomUserDetails user){
    //TODO: 유저 아이디를 나중에 어노테이션으로 가져올 예정
    userService.changePassword(user.getId(), changePasswordRequest.getOldPassword(), changePasswordRequest.getNewPassword());

    return ResponseEntity.ok().build();
  }

  @Auth
  @PutMapping("/profile")
  public ResponseEntity<ChangeProfileResponse> changeProfileImage(@CurrentUser CustomUserDetails user, @RequestParam() MultipartFile file) {
    String profileUrl = uploadService.uploadImg(file);
    ChangeProfileResponse changeProfileResponse = userService.changeProfileUrl(user.getId(), profileUrl);
    return ResponseEntity.ok(changeProfileResponse);
  }

  @Auth
  @DeleteMapping("")
  public ResponseEntity delete(@CurrentUser CustomUserDetails user){
    //TODO: 유저 아이디를 나중에 어노테이션으로 가져올 예정
    userService.delete(user.getId());

    return ResponseEntity.noContent().build();
  }
}
