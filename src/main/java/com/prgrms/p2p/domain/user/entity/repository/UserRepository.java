package com.prgrms.p2p.domain.user.entity.repository;

import com.prgrms.p2p.domain.user.entity.User;
import com.prgrms.p2p.domain.user.entity.config.security.CustomUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<CustomUserDetails> findByEmail(String email);
}
