package com.prgrms.p2p.domain.user.config.config;

import com.prgrms.p2p.domain.user.config.security.JwtAuthenticationFilter;
import com.prgrms.p2p.domain.user.config.security.JwtEntryPoint;
import com.prgrms.p2p.domain.user.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private final JwtEntryPoint jwtEntryPoint;
  private final CustomUserDetailService userDetailsService;

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  @Bean
  public PasswordEncoder passwordEncoder(){
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .httpBasic().disable() // rest api 만을 고려하여 기본 설정은 해제하겠습니다.
        .csrf().disable() // csrf 보안 토큰 disable처리.
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반 인증이므로 세션 역시 사용하지 않습니다.
        .and()
        .authorizeRequests() // 요청에 대한 사용권한 체크
        //회원
        .antMatchers(HttpMethod.GET,"/api/v1/users").hasRole("USER")
        .antMatchers(HttpMethod.POST,"/api/v1/users/password").hasRole("USER")
        .antMatchers(HttpMethod.PUT,"/api/v1/users/users", "/api/v1/users/users").hasRole("USER")
        .antMatchers(HttpMethod.DELETE,"/api/v1/users").hasRole("USER")
        //장소
        .antMatchers(HttpMethod.POST,"/api/v1/places").hasRole("USER")
        //코스
        .antMatchers(HttpMethod.POST,"/api/v1/courses").hasRole("USER")
        .antMatchers(HttpMethod.PUT,"/api/v1/courses").hasRole("USER")
        .antMatchers(HttpMethod.DELETE,"/api/v1/courses/**").hasAnyRole("USER","ADMIN")
        //댓글
        .antMatchers(HttpMethod.POST,"/api/v1/courses/**/comments","/api/v1/places/**/comments").hasRole("USER")
        .antMatchers(HttpMethod.PUT,"/api/v1/courses/**/comments/**","/api/v1/places/**/comments/**").hasRole("USER")
        .antMatchers(HttpMethod.DELETE,"/api/v1/courses/**/comments/**","/api/v1/places/**/comments/**").hasAnyRole("USER","ADMIN")
//        .antMatchers(HttpMethod.GET,"/api/v1/comments").hasRole("USER")
        //좋아요
        .antMatchers(HttpMethod.GET,"/api/v1/likes/places/**","/api/v1/likes/courses/**").hasRole("USER")
        //북마크
        .antMatchers(HttpMethod.GET,"/api/v1/bookmarks/places/**","/api/v1/bookmarks/courses/**").hasRole("USER")
        .anyRequest().permitAll() // 그외 나머지 요청은 누구나 접근 가능
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(jwtEntryPoint)
        .and()
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
  }
}
