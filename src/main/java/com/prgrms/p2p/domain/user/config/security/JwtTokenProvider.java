package com.prgrms.p2p.domain.user.config.security;

import static com.prgrms.p2p.domain.user.config.security.JwtExpirationEnum.ACCESS_TOKEN_EXPIRATION_TIME;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@PropertySource("classpath:application.yaml")
public class JwtTokenProvider {

  @Value("${jwt.secretKey}")
  private String secretKey; // yaml에서 부르기

  private long tokenValidTime = 60 * 60 * 1000L;

  private final UserDetailsService userDetailsService;
  private static final String HEADER_PREFIX = "Bearer ";

  public JwtTokenProvider(
      UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @PostConstruct
  protected void init() {
    secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
  }

  public String generateAccessToken(String username) {
    return generateToken(username, ACCESS_TOKEN_EXPIRATION_TIME.getValue());
  }

  public String getUserEmail(String token) {
    return extractClaims(token).getSubject();
  }

  public Authentication getAuthentication(String token, UserDetails userDetails) {
    return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
  }

  // Request의 Header에서 token 값을 가져옵니다. "Authorization" : "TOKEN값'
  public String resolveToken(HttpServletRequest request) {
    String header = request.getHeader("Authorization");
    if(StringUtils.hasText(header) && header.startsWith(HEADER_PREFIX)) return header.substring(7);
    return null;
  }

  // 토큰의 유효성 + 만료일자 확인
  public boolean validateToken(String token, UserDetails userDetails) {
    String userEmail = getUserEmail(token);
    return userEmail.equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

  private String generateToken(String userEmail, Long expireTime) {
    Claims claims = Jwts.claims().setSubject(userEmail);
    Date now = new Date();

    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + expireTime))
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
  }

  private Claims extractClaims(String token) {
    return Jwts.parser()
        .setSigningKey(secretKey)
        .parseClaimsJws(token)
        .getBody();
  }

  private Key getSignKey(String secretKey) {
    byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  private Boolean isTokenExpired(String token) {
    Date expiration = extractClaims(token).getExpiration();
    return expiration.before(new Date());
  }

}
