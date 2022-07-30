package com.prgrms.p2p.domain.course.entity;


import com.prgrms.p2p.domain.common.BaseEntity;
import com.prgrms.p2p.domain.place.entity.Place;
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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "course_place")
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE room SET is_deleted = true WHERE id = ?")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CoursePlace extends BaseEntity {

  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  @Column(name = "index")
  private Integer index;

  @Column(name = "kakao_map_id")
  private String kakaoMapId;

  @Column(name = "description")
  private String description;

  @Column(name = "image_url")
  private String imageUrl;

  @Column(name = "recommended")
  private Boolean recommended;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "course_id")
  private Course course;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "place_id")
  private Place place;

  @Column(name = "is_deleted")
  private Boolean isDeleted = Boolean.FALSE;

  public CoursePlace(Integer index, String kakaoMapId, String description, String imageUrl,
      boolean recommended, Course course, Place place) {
    this.index = index;
    this.kakaoMapId = kakaoMapId;
    this.description = description;
    this.imageUrl = imageUrl;
    this.recommended = recommended;
    this.course = course;
    this.place = place;
  }

  public void addCourse(Course course) {
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
    if (this.place != null) {
      this.place.getCoursePlaces().remove(this);
    }
    place.addCoursePlace(this);
    this.place = place;
  }

  private void setDescription(String description) {
    this.description = description;
  }

  private void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  private void setRecommended(boolean recommended) {
    this.recommended = recommended;
  }

  private void setCourse(Course course) {
    this.course = course;
  }

  private void setPlace(Place place) {
    this.place = place;
  }

  private void setIndex(Long index) {
    this.index = index;
  }
}
