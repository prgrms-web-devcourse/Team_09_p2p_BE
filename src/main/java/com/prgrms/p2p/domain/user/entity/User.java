package com.prgrms.p2p.domain.user.entity;

import com.prgrms.p2p.domain.comment.entity.Comment;
import com.prgrms.p2p.domain.common.BaseEntity;
import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.place.entity.Place;
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

  private String email;

  private String password;

  private String nickname;

  private LocalDate birth;

  @Enumerated(EnumType.STRING)
  private Sex sex;

  private String profileUrl;

  private LocalDateTime deletedAt;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL ,orphanRemoval = true)
  private List<Course> courses = new ArrayList<>();

  @Column(name = "is_deleted")
  private Boolean isDeleted = Boolean.FALSE;

  public void addCourse(Course course) {
    this.courses.add(course);
  }
}
