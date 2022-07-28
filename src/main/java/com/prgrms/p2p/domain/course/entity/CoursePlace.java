package com.prgrms.p2p.domain.course.entity;


import com.prgrms.p2p.domain.common.BaseEntity;
import com.prgrms.p2p.domain.place.entity.Place;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
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

  @Column(name = "description")
  private String description;

  @Column(name = "imageUrl")
  private String imageUrl;

  @Column(name = "recommended")
  private boolean recommended;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "course_id")
  private Course course;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "place_id")
  private Place place;

  // TODO: 2022/07/27 LinkedList 구현. 참조 : https://stackoverflow.com/questions/64096476/how-to-create-linkedlist-of-jpa-entities
  // Sets the persist operation persist also successors
  @ManyToMany(cascade = CascadeType.PERSIST)
  private Set<CoursePlace> successors = new HashSet<>();

  // Sets the persist operation persist also predecessors
  @ManyToMany(cascade = CascadeType.PERSIST)
  private Set<CoursePlace> predecessors = new HashSet<>();

  @Column(name = "is_deleted")
  private Boolean isDeleted = Boolean.FALSE;

  // TODO: 2022/07/28 생성자 나중에 만들기

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

  private void setSuccessors(Set<CoursePlace> successors) {
    this.successors = successors;
  }

  private void setPredecessors(Set<CoursePlace> predecessors) {
    this.predecessors = predecessors;
  }
}
