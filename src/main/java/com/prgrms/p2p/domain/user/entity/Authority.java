package com.prgrms.p2p.domain.user.entity;

import com.prgrms.p2p.domain.common.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Table(name = "authority")
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority extends BaseEntity implements GrantedAuthority {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "role")
  private String role;

  public Authority(Long id, User user, String role) {
    this.id = id;
    this.user = user;
    this.role = role;
  }

  @Override
  public String getAuthority() {
    return role;
  }

  public static Authority ofUser(User user) {
    return Authority.builder()
        .role("ROLE_USER")
        .user(user)
        .build();
  }

  public static Authority ofAdmin(User user) {
    return Authority.builder()
        .role("ROLE_ADMIN")
        .user(user)
        .build();
  }
}
