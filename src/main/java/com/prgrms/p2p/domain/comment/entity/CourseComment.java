package com.prgrms.p2p.domain.comment.entity;

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
@Table(name = "course_comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseComment extends Comment {

  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "course_id")
  private Course course;

  @Column(name = "visibility")
  private Visibility visibility = Visibility.TRUE;

  public CourseComment(String comment, Long rootCommentId, Long userId,
      Course course, Long seq) {
    super(comment, rootCommentId, userId, seq);
    this.addCourse(course);
  }

  public void addCourse(Course course) {
    if (this.course != null) {
      this.course.getCourseComments().remove(this);
    }
    course.addCourseComment(this);
    this.course = course;
  }

  public void changeVisibility(Visibility visibility) {
    setVisibility(visibility);
  }
  private void setCourse(Course course) {
    this.course = course;
  }

  private void setVisibility(Visibility visibility) {
    this.visibility = visibility;
  }
}
