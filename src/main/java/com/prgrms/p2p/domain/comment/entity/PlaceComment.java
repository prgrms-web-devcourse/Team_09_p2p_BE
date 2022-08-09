package com.prgrms.p2p.domain.comment.entity;

import com.prgrms.p2p.domain.common.exception.BadRequestException;
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

  @Column(name = "visibility")
  private Visibility visibility = Visibility.TRUE;

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
    setPlace(place);
  }

  public void changeVisibility(Visibility visibility) {
    setVisibility(visibility);
  }

  private void setPlace(Place place) {
    validateNull(place, "장소는 null 일 수 없습니다.");
    this.place = place;
  }

  private void setVisibility(Visibility visibility) {
    validateNull(visibility, "댓글 노출여부는 null 일 수 없습니다.");
    this.visibility = visibility;
  }

  private void validateNull(Object comment, String message) {
    if (Objects.isNull(comment)) {
      throw new BadRequestException(message);
    }
  }
}
