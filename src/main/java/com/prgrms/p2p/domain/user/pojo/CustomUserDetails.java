package com.prgrms.p2p.domain.user.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prgrms.p2p.domain.user.entity.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Builder
public class CustomUserDetails implements UserDetails {

  private String email;
  private String password;

  @Builder.Default
  private List<String> roles = new ArrayList<>();

  public CustomUserDetails() {
  }

  public CustomUserDetails(String email, String password, List<String> roles) {
    this.email = email;
    this.password = password;
    this.roles = roles;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream()
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return false;
  }

  @Override
  @JsonIgnore
  public boolean isAccountNonLocked() {
    return false;
  }

  @Override
  @JsonIgnore
  public boolean isCredentialsNonExpired() {
    return false;
  }

  @Override
  @JsonIgnore
  public boolean isEnabled() {
    return false;
  }

  public static CustomUserDetails of(User user) {
    return CustomUserDetails.builder()
        .email(user.getEmail())
        .password(user.getPassword())
        .build();
  }
}
