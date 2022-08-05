package com.prgrms.p2p.domain.user.config.security;

import static com.prgrms.p2p.domain.user.config.security.JwtExpirationEnum.LOGIN_EXPIRATION_TIME;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.p2p.domain.user.exception.LoginFailException;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.joda.time.LocalDateTime;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RedisInterceptor implements HandlerInterceptor {

  private static final Logger logger = LoggerFactory.getLogger(RedisInterceptor.class);

  private final RedisTemplate redisTemplate;

  public RedisInterceptor(RedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    if(HttpMethod.OPTIONS.matches(request.getMethod())) return true;
    this.readyRequestBody(request, response);
    return HandlerInterceptor.super.preHandle(request, response, handler);
  }

  private void readyRequestBody(HttpServletRequest request, HttpServletResponse response)
      throws ParseException, IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> reqBody = objectMapper.readValue(request.getInputStream(), Map.class);

    logger.info("요청 정보: " + reqBody);
    logger.info("요청 URL: " + request.getRequestURL());

    String email = (String) reqBody.get("email");

    Boolean count1 = redisTemplate.opsForHash().hasKey(email, "count");
    if(!count1) {
      HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
      hash.put(email, "count", "0");

      String expiredAt = String.valueOf(
          LocalDateTime.fromDateFields(
              new Date(new Date().getTime() + LOGIN_EXPIRATION_TIME.getValue())));

      hash.put(email, "expiredAt", expiredAt);
      redisTemplate.expire(email, LOGIN_EXPIRATION_TIME.getValue(), TimeUnit.MILLISECONDS);
      return;
    }
    else {
      Object o = redisTemplate.opsForHash().get(email, "count");
      int count = Integer.parseInt(String.valueOf(o));

      if(count >= 5) {
        Object ex = redisTemplate.opsForHash().get(email, "expiredAt");
        String expiredAt = String.valueOf(ex);
        throw new LoginFailException(5, expiredAt);
      }
    }
  }

}
