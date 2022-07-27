package com.prgrms.p2p.domain.comment.entity;

import com.prgrms.p2p.domain.place.entity.Place;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
@DiscriminatorValue("place_comment")
public class PlaceComment extends Comment {

  @ManyToOne(fetch = FetchType.LAZY)
  private Place place;
}
