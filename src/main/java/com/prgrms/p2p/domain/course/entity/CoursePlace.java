package com.prgrms.p2p.domain.course.entity;


import com.prgrms.p2p.domain.common.BaseEntity;
import com.prgrms.p2p.domain.place.entity.Place;
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
import org.apache.logging.log4j.util.Strings;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "course_place")
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE course_place SET is_deleted = true WHERE id = ?")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CoursePlace extends BaseEntity {

  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  @Column(name = "seq")
  private Integer seq;

  @Column(name = "description")
  private String description;

  @Column(name = "image_url")
  private String imageUrl;

  @Column(name = "recommended")
  private Boolean recommended = Boolean.FALSE;

  @Column(name = "thumbnailed")
  private Boolean thumbnail = Boolean.FALSE;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "course_id")
  private Course course;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "place_id")
  private Place place;

  @Column(name = "is_deleted")
  private Boolean isDeleted = Boolean.FALSE;

  public CoursePlace(Integer seq, String description, String imageUrl, Boolean recommended,
      Boolean thumbnail, Course course, Place place) {
    setSeq(seq);
    setDescription(description);
    setImageUrl(imageUrl);
    setRecommended(recommended);
    setThumbnail(thumbnail);
    addCourse(course);
    addPlace(place);
  }


  public void addCourse(Course course) {
    if (Objects.isNull(course)) {
      throw new IllegalArgumentException();
    }
    if (this.course != null) {
      this.course.getCoursePlaces().remove(this);
    }
    course.addCoursePlace(this);
    this.course = course;
  }

  public void deleteCourse(Course course) {
    course.deleteCoursePlace(this);
    this.course = null;
  }

  public void addPlace(Place place) {
    if (Objects.isNull(place)) {
      throw new IllegalArgumentException();
    }
    if (this.place != null) {
      this.place.getCoursePlaces().remove(this);
    }
    place.addCoursePlace(this);
    this.place = place;
  }

  private void setDescription(String description) {
    if (Strings.isBlank(description)) {
      throw new IllegalArgumentException();
    }
    this.description = description;
  }

  private void setImageUrl(String imageUrl) {
    if (Strings.isBlank(imageUrl)) {
      throw new IllegalArgumentException();
    }
    this.imageUrl = imageUrl;
  }

  private void setRecommended(Boolean recommended) {
    this.recommended = recommended;
  }

  private void setThumbnail(Boolean thumbnailed) {
    this.thumbnail = thumbnailed;
  }

  private void setCourse(Course course) {
    if (Objects.isNull(course)) {
      throw new IllegalArgumentException();
    }
    this.course = course;
  }

  private void setPlace(Place place) {
    if (Objects.isNull(course)) {
      throw new IllegalArgumentException();
    }
    this.place = place;
  }

  private void setSeq(Integer index) {
    if (Objects.isNull(index)) {
      throw new IllegalArgumentException();
    }
    if (index < 0) {
      throw new IllegalArgumentException();
    }
    this.seq = index;
  }
}
