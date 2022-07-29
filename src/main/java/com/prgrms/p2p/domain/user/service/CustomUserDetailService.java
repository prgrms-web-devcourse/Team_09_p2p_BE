package com.prgrms.p2p.domain.user.service;

import com.prgrms.p2p.domain.user.entity.User;
import com.prgrms.p2p.domain.user.pojo.CustomUserDetails;
import com.prgrms.p2p.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

  private final UserRepository userRepository;


  @Override
  public UserDetails loadUserByUsername(String username){
    User user = userRepository.findByEmail(username)
        .orElseThrow(() -> new IllegalArgumentException("아이디를 찾을 수 없습니다."));
    return CustomUserDetails.of(user);
  }
}
