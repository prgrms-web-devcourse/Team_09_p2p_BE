package com.prgrms.p2p.domain.bookmark.entity;

import com.prgrms.p2p.domain.course.entity.Course;
import java.util.Objects;
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

  public CourseBookmark(Long userId, Course course) {
    super(userId);
    setCourse(course);
  }

  private void setCourse(Course course) {
    if (Objects.isNull(course)) {
      throw new IllegalArgumentException("코스로 빈 값을 받을 수 없습니다.");
    }
    if (Objects.nonNull(this.course)) {
      this.course.getCourseBookmarks().remove(this);
    }
    this.course = course;
    course.addCourseBookmark(this);
  }

  public void deleteCourse(Course course) {
    course.deleteCourseBookmark(this);
    this.course = null;
  }
}
