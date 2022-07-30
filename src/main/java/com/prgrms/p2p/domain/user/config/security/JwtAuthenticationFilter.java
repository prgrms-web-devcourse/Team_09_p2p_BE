package com.prgrms.p2p.domain.user.config.security;

import com.prgrms.p2p.domain.user.repository.LogoutTokenRedisRepository;
import com.prgrms.p2p.domain.user.service.CustomUserDetailService;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

  private final JwtTokenProvider jwtTokenProvider;
  private final LogoutTokenRedisRepository logoutTokenRedisRepository;
  private final CustomUserDetailService customUserDetailService;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);

    if(token != null){
      checkLogout(token);

      String userEmail = jwtTokenProvider.getUserEmail(token);
      if(userEmail != null) {
        UserDetails userDetails = customUserDetailService.loadUserByUsername(userEmail);

        validateAccessToken(token, userDetails);
        Authentication authentication = jwtTokenProvider.getAuthentication(token, userDetails);
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    }
    chain.doFilter(request,response);
  }

  //TODO: 로그아웃 관련 예외 만들기
  private void checkLogout(String token) {
    logoutTokenRedisRepository.existsByLogoutToken(token)
        .orElseThrow(IllegalArgumentException::new);
  }

  //TODO: 토큰 인증 실패 관련 예외 만들기
  private void validateAccessToken(String token, UserDetails userDetails) {
    if(jwtTokenProvider.validateToken(token, userDetails)) throw new IllegalArgumentException();
  }

  private void setSecurityAuthentication(HttpServletRequest request, UserDetails userDetails) {
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
        userDetails, "", userDetails.getAuthorities());

    usernamePasswordAuthenticationToken.setDetails(
        new WebAuthenticationDetailsSource()
            .buildDetails(request));

    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
  }
}
