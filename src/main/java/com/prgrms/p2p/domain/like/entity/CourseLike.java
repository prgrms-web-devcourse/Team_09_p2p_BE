package com.prgrms.p2p.domain.like.entity;

import com.prgrms.p2p.domain.course.entity.Course;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "course_like")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseLike extends Like {

  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "course_id")
  private Course course;

  public CourseLike(Long userId, Course course) {
    super(userId);
    this.addCourse(course);
  }

  public void addCourse(Course course) {
    if (this.course != null) {
      this.course.getCourseLikes().remove(this);
    }
    course.addCourseLike(this);
    this.course = course;
  }

  public void deleteCourse(Course course) {
    course.deleteCourseLike(this);
    this.course = course;
  }

  private void setCourse(Course course) {
    this.course = course;
  }
}
