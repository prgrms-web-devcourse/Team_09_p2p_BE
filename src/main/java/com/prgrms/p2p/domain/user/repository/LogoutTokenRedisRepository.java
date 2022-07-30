package com.prgrms.p2p.domain.user.repository;

import com.prgrms.p2p.domain.user.pojo.LogoutToken;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface LogoutTokenRedisRepository extends CrudRepository<LogoutToken, String> {

  Optional<LogoutToken> existsByLogoutToken(String token);

}
