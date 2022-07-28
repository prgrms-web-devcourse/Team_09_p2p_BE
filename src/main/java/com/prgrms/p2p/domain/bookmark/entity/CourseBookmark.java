package com.prgrms.p2p.domain.bookmark.entity;

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
@Table(name = "course_bookmark")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseBookmark extends Bookmark {

  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "course_id")
  private Course course;

  public CourseBookmark(String comment, Long userId, Course course) {
    super(comment, userId);
    this.addCourse(course);
  }

  public void addCourse(Course course) {
    if (this.course != null) {
      this.course.getCourseBookmarks().remove(this);
    }
    course.addCourseBookmark(this);
    this.course = course;
  }

  public void deleteCourse(Course course) {
    course.deleteCourseBookmark(this);
    this.course = null;
  }

  private void setCourse(Course course) {
    this.course = course;
  }
}
