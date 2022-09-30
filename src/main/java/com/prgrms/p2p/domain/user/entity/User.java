package com.prgrms.p2p.domain.user.entity;

import static java.util.Objects.isNull;
import static org.apache.logging.log4j.util.Strings.isBlank;

import com.prgrms.p2p.domain.common.BaseEntity;
import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.user.exception.InvalidPatternException;
import com.prgrms.p2p.domain.user.util.PasswordEncrypter;
import com.sun.istack.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "users")
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE id = ?")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  @Column(name = "email")
  private String email;

  @Column(name = "password")
  private String password;

  @Column(name = "nickname")
  private String nickname;

  @Column(name = "birth")
  private LocalDate birth;

  @Column(name = "score")
  private int score =0;

  @Enumerated(EnumType.STRING)
  @Column(name = "sex")
  private Sex sex;

  @Column(name = "profile_url")
  private String profileUrl;

  @BatchSize(size = 100)
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL ,orphanRemoval = true)
  private List<Course> courses = new ArrayList<>();

  @Column(name = "is_deleted")
  private Boolean isDeleted = Boolean.FALSE;

  @BatchSize(size = 100)
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Authority> authorities = new HashSet<>();

  public void addCourse(Course course) {
    this.courses.add(course);
  }

  public User(String email, String password, String nickname, String birth, Sex sex) {
    setEmail(email);
    setPassword(password);
    setNickname(nickname);
    setBirth(birth);
    setSex(sex);
    authorities = new HashSet<>();
  }

  public User(Long id, String email, String password, String nickname, LocalDate birth,
      Sex sex, String profileUrl, List<Course> courses, Boolean isDeleted,
      Set<Authority> authorities) {
    this.id = id;
    this.email = email;
    this.password = password;
    this.nickname = nickname;
    this.birth = birth;
    this.sex = sex;
    this.profileUrl = profileUrl;
    this.courses = courses;
    this.isDeleted = isDeleted;
    this.authorities = authorities;
  }

  public List<String> getAuthorities() {
    return authorities.stream()
        .map(Authority::getRole)
        .collect(Collectors.toList());
  }

  public void addAuthority(Authority authority) {
    authorities.add(authority);
  }

  public boolean matchPassword(String password) {
    if(!PasswordEncrypter.isMatch(password, this.password)) return false;
    return true;
  }

  public void changePassword(String newPassword) {
    setPassword(newPassword);
  }

  public void changeNickname(String nickname) {
    setNickname(nickname);
  }

  public void changeBirth(String birth) { setBirth(birth); }

  public void changeSex(Sex sex) {
    setSex(sex);
  }

  public void changeProfileUrl(String profileUrl) {
    setProfileUrl(profileUrl);
  }

  public Optional<String> getProfileUrl() {
    if(this.profileUrl == null) {
      return Optional.empty();
    } else {
      return Optional.of(this.profileUrl);
    }
  }

  private void checkBlank(String target) {
    if(isBlank(target)) {
      throw new InvalidPatternException();
    }
  }

  private void setEmail(String email) {
    checkBlank(email);
    this.email = email;
  }

  private void setPassword(String password) {
    checkBlank(password);
    this.password = password;
  }

  private void setNickname(String nickname) {
    checkBlank(nickname);
    this.nickname = nickname;
  }

  private void setBirth(String birth) {
    checkBlank(birth);
    this.birth = LocalDate.parse(birth);
  }
  private void setSex(Sex sex) {
    if(isNull(sex)) throw new InvalidPatternException();
    this.sex = sex;
  }

  private void setProfileUrl(String profileUrl) {
    checkBlank(profileUrl);
    this.profileUrl = profileUrl;
  }
}
