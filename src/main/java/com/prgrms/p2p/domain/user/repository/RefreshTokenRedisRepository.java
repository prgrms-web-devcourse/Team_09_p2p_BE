package com.prgrms.p2p.domain.user.repository;

import com.prgrms.p2p.domain.user.pojo.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, Long> {

}
