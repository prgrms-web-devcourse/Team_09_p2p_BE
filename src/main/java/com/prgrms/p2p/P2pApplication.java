package com.prgrms.p2p;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class P2pApplication {

  public static void main(String[] args) {
    SpringApplication.run(P2pApplication.class, args);
  }

}
