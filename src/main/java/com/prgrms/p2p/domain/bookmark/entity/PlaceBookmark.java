package com.prgrms.p2p.domain.bookmark.entity;

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

@Entity
@Table(name = "place_bookmark")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceBookmark extends Bookmark {

  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "place_id")
  private Place place;

  public PlaceBookmark(Long userId, Place place) {
    super(userId);
    this.addPlace(place);
  }

  public void addPlace(Place place) {
    if (this.place != null) {
      this.place.getPlaceBookmarks().remove(this);
    }
    place.addPlaceBookmark(this);
    this.place = place;
  }

  public void deletePlace(Place place) {
    place.deleteBookmark(this);
    this.place = null;
  }

  private void setPlace(Place place) {
    this.place = place;
  }
}
