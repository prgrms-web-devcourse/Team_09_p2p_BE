package com.prgrms.p2p.domain.user.controller;

import com.prgrms.p2p.domain.user.config.security.JwtTokenProvider;
import com.prgrms.p2p.domain.user.dto.ChangePasswordRequest;
import com.prgrms.p2p.domain.user.dto.LoginRequest;
import com.prgrms.p2p.domain.user.dto.LoginResponse;
import com.prgrms.p2p.domain.user.dto.ModifyRequest;
import com.prgrms.p2p.domain.user.dto.SignUpRequest;
import com.prgrms.p2p.domain.user.service.UserService;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;
  private final JwtTokenProvider jwtTokenProvider;

  public UserController(UserService userService,
      JwtTokenProvider jwtTokenProvider) {
    this.userService = userService;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @PostMapping("/")
  public ResponseEntity<String> signUp(@RequestBody SignUpRequest signUpRequest) {
    String nickname = userService.signUp(signUpRequest);
    return ResponseEntity.created(URI.create("/")).body(nickname);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

    //TODO: UnAuthorizedAccessException 만들기
    LoginResponse login = userService.login(loginRequest.getEmail(), loginRequest.getPassword())
        .orElseThrow(RuntimeException::new);

    return ResponseEntity.ok(login);
  }

  //TODO: 유저권한 확인 추가 필요
  @PutMapping("/")
  public ResponseEntity modify(@RequestBody ModifyRequest modifyRequest) {
    userService.modify(modifyRequest.getId(), modifyRequest.getNickname(), modifyRequest.getBirth(),
        modifyRequest.getSex());

    return ResponseEntity.ok().build();
  }

  //TODO: 유저권한 확인 추가 필요
  @PostMapping("/password")
  public ResponseEntity changePassword(@RequestBody ChangePasswordRequest changePasswordRequest){
    //TODO: 유저 아이디를 나중에 어노테이션으로 가져올 예정
    userService.changePassword(1L,changePasswordRequest.getOldPassword(),changePasswordRequest.getNewPassword());

    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/")
  public ResponseEntity delete(){
    //TODO: 유저 아이디를 나중에 어노테이션으로 가져올 예정
    userService.delete(1L);

    return ResponseEntity.ok().build();
  }

  @PostMapping("/email")
  public void validateEmail(@RequestBody String email) {
    userService.validateEmail(email);
  }

  @PostMapping("/nickname")
  public void validateNickname(@RequestBody String nickname) {
    userService.validateNickname(nickname);
  }
}
