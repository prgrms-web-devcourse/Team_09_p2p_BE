package com.prgrms.p2p.domain.place.repository;

import static com.prgrms.p2p.domain.place.entity.QPlace.place;

import com.prgrms.p2p.domain.place.dto.SearchPlaceRequest;
import com.prgrms.p2p.domain.place.entity.Category;
import com.prgrms.p2p.domain.place.entity.Place;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.util.ObjectUtils;

public class PlaceSearchRepositoryImpl extends QuerydslRepositorySupport implements PlaceSearchRepository {

  public PlaceSearchRepositoryImpl() {
    super(Place.class);
  }

  @Override
  public Slice<Place> searchPlace(SearchPlaceRequest searchPlaceRequest, Pageable pageable) {
    JPQLQuery<Place> placeJPQLQuery = from(place)
        .where(
            keywordListContains(searchPlaceRequest.getKeyword()),
            categoryEq(searchPlaceRequest.getCategory())
        )
        .select(place);

    List<Place> placeList = getQuerydsl().applyPagination(pageable, placeJPQLQuery).fetch();

    boolean hasNext = false;

    if (placeList.size() > pageable.getPageSize()) {
      placeList.remove(pageable.getPageSize());
      hasNext = true;
    }

    return new SliceImpl<>(placeList, pageable, hasNext);
  }

  private BooleanBuilder keywordListContains(String keyword) {
    if (ObjectUtils.isEmpty(keyword)) {
      return null;
    }
    BooleanBuilder builder = new BooleanBuilder();
    String[] splitedKeyword = keyword.split(" ");
    for (String value : splitedKeyword) {
      builder.and(place.name.containsIgnoreCase(value));
    }
    return builder;
  }

  private BooleanExpression categoryEq(String category) {
    return ObjectUtils.isEmpty(category) ? null : place.category.eq(Category.valueOf(category));
  }
}