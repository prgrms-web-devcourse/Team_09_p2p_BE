package com.prgrms.p2p.domain.place.entity;

import com.prgrms.p2p.domain.bookmark.entity.PlaceBookmark;
import com.prgrms.p2p.domain.comment.entity.PlaceComment;
import com.prgrms.p2p.domain.common.BaseEntity;
import com.prgrms.p2p.domain.course.entity.CoursePlace;
import com.prgrms.p2p.domain.like.entity.PlaceLike;
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

  @Column(name = "kakao_map_id")
  private Long kakaoMapId;

  @Column(name = "name")
  private String name;

  @Embedded
  @Column(name = "address")
  private Address address;

  @Column(name = "latitude")
  private String latitude;

  @Column(name = "longitude")
  private String longitude;

  @Enumerated(EnumType.STRING)
  @Column(name = "category")
  private Category category;

  @Embedded
  @Column(name = "phone_number")
  private PhoneNumber phoneNumber;

  @Column(name = "image_url")
  private String imageUrl;

  @OneToMany(mappedBy = "place")
  private List<CoursePlace> coursePlaces = new ArrayList<>();

  @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PlaceComment> placeComments = new ArrayList<>();

  @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PlaceBookmark> placeBookmarks = new ArrayList<>();

  @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PlaceLike> placeLikes = new ArrayList<>();

  @Column(name = "is_deleted")
  private Boolean isDeleted = Boolean.FALSE;

  public Place(Long kakaoMapId, String name, Address address, String latitude,
      String longitude, Category category, PhoneNumber phoneNumber, String imageUrl) {
    this.kakaoMapId = kakaoMapId;
    this.name = name;
    this.address = address;
    this.latitude = latitude;
    this.longitude = longitude;
    this.category = category;
    this.phoneNumber = phoneNumber;
    this.imageUrl = imageUrl;
  }

  // TODO: 2022/07/28 변경 로직은 추후 생성

  public void addCoursePlace(CoursePlace coursePlace) {
    this.coursePlaces.add(coursePlace);
  }

  public void addPlaceComment(PlaceComment placeComment) {
    this.placeComments.add(placeComment);
  }

  public void addPlaceBookmark(PlaceBookmark placeBookmark) {
    this.placeBookmarks.add(placeBookmark);
  }

  public void deleteBookmark(PlaceBookmark placeBookmark) {
    this.placeBookmarks.remove(placeBookmark);
  }

  public void addPlaceLike(PlaceLike placeLike) {
    this.placeLikes.add(placeLike);
  }

  public void deletePlaceLike(PlaceLike placeLike) {
    this.placeLikes.remove(placeLike);
  }

  private void setKakaoMapId(Long kakaoMapId) {
    this.kakaoMapId = kakaoMapId;
  }

  private void setName(String name) {
    this.name = name;
  }

  private void setAddress(Address address) {
    this.address = address;
  }

  private void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  private void setLongitude(String longitude) {
    this.longitude = longitude;
  }

  private void setCategory(Category category) {
    this.category = category;
  }

  private void setPhoneNumber(PhoneNumber phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  private void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  private void setCoursePlaces(List<CoursePlace> coursePlaces) {
    this.coursePlaces = coursePlaces;
  }

  private void setPlaceComments(
      List<PlaceComment> placeComments) {
    this.placeComments = placeComments;
  }

  private void setPlaceBookmarks(
      List<PlaceBookmark> placeBookmarks) {
    this.placeBookmarks = placeBookmarks;
  }

  private void setPlaceLikes(List<PlaceLike> placeLikes) {
    this.placeLikes = placeLikes;
  }
}
