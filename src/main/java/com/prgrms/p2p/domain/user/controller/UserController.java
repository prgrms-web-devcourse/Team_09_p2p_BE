package com.prgrms.p2p.domain.user.controller;

import com.prgrms.p2p.domain.user.dto.SignUpRequest;
import com.prgrms.p2p.domain.user.service.UserService;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/")
  public ResponseEntity<String> signUp(@RequestBody SignUpRequest signUpRequest) {
    String nickname = userService.signUp(signUpRequest);
    return ResponseEntity.created(URI.create("/")).body(nickname);
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
