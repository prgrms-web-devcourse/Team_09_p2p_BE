package com.prgrms.p2p.domain.user.entity;

import static org.apache.logging.log4j.util.Strings.isBlank;

import com.prgrms.p2p.domain.comment.entity.Comment;
import com.prgrms.p2p.domain.common.BaseEntity;
import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.place.entity.Place;
import com.prgrms.p2p.domain.user.util.PasswordEncrypter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "user")
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE room SET is_deleted = true WHERE id = ?")
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

  @Enumerated(EnumType.STRING)
  @Column(name = "sex")
  private Sex sex;

  @Column(name = "profile_url")
  private String profileUrl;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL ,orphanRemoval = true)
  private List<Course> courses = new ArrayList<>();

  @Column(name = "is_deleted")
  private Boolean isDeleted = Boolean.FALSE;

  public void addCourse(Course course) {
    this.courses.add(course);
  }

  public User(String email, String password, String nickname, String birth, Sex sex) {
    setEmail(email);
    setPassword(password);
    setNickname(nickname);
    setBirth(birth);
    setSex(sex);
  }

  public void matchPassword(String password) {
    //TODO: 예외 바꿔주기
    String encrypted = PasswordEncrypter.encrypt(password);
    if(!PasswordEncrypter.isMatch(encrypted, this.password)) {
      throw new IllegalArgumentException();
    }
  }

  public void changePassword(String newPassword) {
    setPassword(newPassword);
  }

  public void changeNickname(String nickname) {
    setNickname(nickname);
  }

  public void changeBirth(String birth) { setBirth(birth); }

  public void changeSex(Sex Sex) {
    setSex(sex);
  }

  private void checkBlank(String target) {
    //TODO: InvalidParamException
    if(isBlank(target)) {
      throw new RuntimeException();
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
    this.sex = sex;
  }

  private void setProfileUrl(String profileUrl) {
    checkBlank(profileUrl);
    this.profileUrl = profileUrl;
  }
}
