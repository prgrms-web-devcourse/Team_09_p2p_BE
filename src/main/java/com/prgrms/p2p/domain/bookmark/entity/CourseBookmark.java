package com.prgrms.p2p.domain.bookmark.entity;

import com.prgrms.p2p.domain.course.entity.Course;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
@DiscriminatorValue("course_bookmark")
public class CourseBookmark extends Bookmark {

  @ManyToOne(fetch = FetchType.LAZY)
  private Course course;
}
