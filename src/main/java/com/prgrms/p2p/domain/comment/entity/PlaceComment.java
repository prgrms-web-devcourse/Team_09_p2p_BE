package com.prgrms.p2p.domain.comment.entity;

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
@Table(name = "place_comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceComment extends Comment {

  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "place_id")
  private Place place;

  public PlaceComment(String comment, Long rootCommentId, Long userId,
      Place place) {
    super(comment, rootCommentId, userId);
    this.addPlace(place);
  }

  public void addPlace(Place place) {
    if (this.place != null) {
      this.place.getPlaceComments().remove(this);
    }
    place.addPlaceComment(this);
    this.place = place;
  }

  private void setPlace(Place place) {
    this.place = place;
  }
}
