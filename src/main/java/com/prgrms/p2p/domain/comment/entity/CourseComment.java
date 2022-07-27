package com.prgrms.p2p.domain.comment.entity;

import com.prgrms.p2p.domain.course.entity.Course;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
@DiscriminatorValue("course_comment")
public class CourseComment extends Comment {

  @ManyToOne(fetch = FetchType.LAZY)
  private Course course;
}
