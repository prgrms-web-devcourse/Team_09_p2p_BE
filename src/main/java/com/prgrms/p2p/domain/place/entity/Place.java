package com.prgrms.p2p.domain.place.entity;

import com.prgrms.p2p.domain.bookmark.entity.Bookmark;
import com.prgrms.p2p.domain.common.BaseEntity;
import com.prgrms.p2p.domain.course.entity.CoursePlace;
import com.prgrms.p2p.domain.like.entity.Like;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "place")
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE room SET is_deleted = true WHERE id = ?")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place extends BaseEntity {

  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  private Long kakaoMapId;

  private String name;

  @Embedded
  private Address address;

  private Double latitude;

  private Double longitude;

  @Enumerated(EnumType.STRING)
  private Category category;

  @Embedded
  private PhoneNumber phoneNumber;

  private String imageUrl;

  @OneToMany(mappedBy = "place")
  private List<CoursePlace> coursePlaces = new ArrayList<>();

  @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Bookmark> bookmarks = new ArrayList<>();

  @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Like> likes = new ArrayList<>();

  @Column(name = "is_deleted")
  private Boolean isDeleted = Boolean.FALSE;
}
