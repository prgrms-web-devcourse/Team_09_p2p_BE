package com.prgrms.p2p.domain.user.config.security;

import com.prgrms.p2p.domain.user.pojo.CustomUserDetails;
import com.prgrms.p2p.domain.user.repository.LogoutTokenRedisRepository;
import com.prgrms.p2p.domain.user.service.CustomUserDetailService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

  private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
  private final JwtTokenProvider jwtTokenProvider;
  private final LogoutTokenRedisRepository logoutTokenRedisRepository;
  private final CustomUserDetailService customUserDetailService;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    try {
      String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);

      if(token != null){

        String userEmail = jwtTokenProvider.getUserEmail(token);
        if(userEmail != null) {
          CustomUserDetails userDetails = customUserDetailService.loadUserByUsername(userEmail);

          validateAccessToken(token, userDetails);
          Authentication authentication = jwtTokenProvider.getAuthentication(userDetails);
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      }
    } catch (SignatureException | MalformedJwtException e) {
      logger.info("토큰이 변조되었습니다.");
    } catch (ExpiredJwtException e) {
      logger.info("토큰이 만료되었습니다.");
    }
    chain.doFilter(request,response);
  }

  //TODO: 로그아웃 관련 예외 만들기
  private void checkLogout(String token) {
    logoutTokenRedisRepository.existsByLogoutToken(token)
        .orElseThrow(IllegalArgumentException::new);
  }

  //TODO: 토큰 인증 실패 관련 예외 만들기
  private void validateAccessToken(String token, CustomUserDetails userDetails) {
    if(!jwtTokenProvider.validateToken(token, userDetails)) throw new IllegalArgumentException();
  }

}