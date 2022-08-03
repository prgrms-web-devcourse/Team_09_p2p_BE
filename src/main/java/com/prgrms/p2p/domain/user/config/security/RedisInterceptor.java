package com.prgrms.p2p.domain.user.config.security;

import static com.prgrms.p2p.domain.user.config.security.JwtExpirationEnum.LOGIN_EXPIRATION_TIME;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
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

    if(!redisTemplate.hasKey(email)) {
      redisTemplate.opsForValue()
          .set(email, String.valueOf(0) ,LOGIN_EXPIRATION_TIME.getValue(), TimeUnit.MILLISECONDS);
      return;
    }
    else {
      Object o = redisTemplate.opsForValue().get(email);
      int count = Integer.parseInt(String.valueOf(o));
      System.out.println(count);
      //TODO : 이미 5번 이상 틀린 유저 예외처리 해주기
      if(count >= 5) throw new RuntimeException();
    }
  }

}
