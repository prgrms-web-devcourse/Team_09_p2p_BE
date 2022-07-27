package com.prgrms.p2p.domain.like.entity;

import com.prgrms.p2p.domain.place.entity.Place;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
@DiscriminatorValue("place_like")
public class PlaceLike extends Like {

  @ManyToOne(fetch = FetchType.LAZY)
  private Place place;
}
