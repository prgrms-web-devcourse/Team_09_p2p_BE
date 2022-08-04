package com.prgrms.p2p.domain.common.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommonController {

  @GetMapping("/")
  public ResponseEntity<String> checkApi() {
    return ResponseEntity.ok("Hello! This is Api for Team F");
  }
}
