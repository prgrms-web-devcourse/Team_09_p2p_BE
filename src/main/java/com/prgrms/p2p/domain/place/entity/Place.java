package com.prgrms.p2p.domain.place.entity;

import com.prgrms.p2p.domain.bookmark.entity.PlaceBookmark;
import com.prgrms.p2p.domain.comment.entity.PlaceComment;
import com.prgrms.p2p.domain.common.BaseEntity;
import com.prgrms.p2p.domain.common.exception.BadRequestException;
import com.prgrms.p2p.domain.course.entity.CoursePlace;
import com.prgrms.p2p.domain.like.entity.PlaceLike;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "place")
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE place SET is_deleted = true WHERE id = ?")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place extends BaseEntity {

  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  @Column(name = "kakao_map_id")
  private String kakaoMapId;

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

  @BatchSize(size = 100)
  @OneToMany(mappedBy = "place")
  private List<CoursePlace> coursePlaces = new ArrayList<>();

  @BatchSize(size = 100)
  @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PlaceComment> placeComments = new ArrayList<>();

  @BatchSize(size = 100)
  @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PlaceBookmark> placeBookmarks = new ArrayList<>();

  @BatchSize(size = 100)
  @OneToMany(mappedBy = "place", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PlaceLike> placeLikes = new ArrayList<>();

  @Column(name = "is_deleted")
  private Boolean isDeleted = Boolean.FALSE;

  public Place(String kakaoMapId, String name, Address address, String latitude,
      String longitude, Category category, PhoneNumber phoneNumber) {
    setKakaoMapId(kakaoMapId);
    setName(name);
    setAddress(address);
    setLatitude(latitude);
    setLongitude(longitude);
    setCategory(category);
    setPhoneNumber(phoneNumber);
  }

  public void changeName(String newName) {
    setName(newName);
  }

  public void changeAddress(Address newAddress) {
    setAddress(newAddress);
  }

  public void changeCategory(Category newCategory) {
    setCategory(newCategory);
  }

  public void changePhoneNumber(PhoneNumber newPhoneNumber) {
    setPhoneNumber(newPhoneNumber);
  }

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

  private void setKakaoMapId(String kakaoMapId) {
    this.kakaoMapId = kakaoMapId;
  }

  private void setName(String name) {
    if (name.isBlank()) {
      throw new BadRequestException("장소 이름이 필요합니다.");
    }
    this.name = name;
  }

  private void setAddress(Address address) {
    this.address = address;
  }

  private void setLatitude(String latitude) {
    if (latitude.isBlank()) {
      throw new BadRequestException("장소 위도가 필요합니다.");
    }
    this.latitude = latitude;
  }

  private void setLongitude(String longitude) {
    if (longitude.isBlank()) {
      throw new BadRequestException("장소 경도가 필요합니다.");
    }
    this.longitude = longitude;
  }

  private void setCategory(Category category) {
    this.category = category;
  }

  private void setPhoneNumber(PhoneNumber phoneNumber) {
    this.phoneNumber = phoneNumber;
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
