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

  private String description;

  private String imageUrl;

  private boolean recommended;

  @ManyToOne(fetch = FetchType.LAZY)
  private Course course;

  @ManyToOne(fetch = FetchType.LAZY)
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
}
