package com.prgrms.p2p.domain.like.entity;

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

@Entity
@Table(name = "place_like")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceLike extends Like {

  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "place_id")
  private Place place;

  public PlaceLike(Long userId, Place place) {
    super(userId);
    this.addPlace(place);
  }

  public void addPlace(Place place) {
    if (Objects.nonNull(this.place)) {
      this.place.getPlaceLikes().remove(this);
    }
    place.addPlaceLike(this);
    this.place = place;
  }

  public void deletePlace(Place place) {
    place.deletePlaceLike(this);
    this.place = null;
  }
}
