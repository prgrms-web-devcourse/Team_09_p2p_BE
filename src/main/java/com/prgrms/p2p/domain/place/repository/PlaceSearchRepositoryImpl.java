package com.prgrms.p2p.domain.place.repository;

import static com.prgrms.p2p.domain.place.entity.QPlace.place;

import com.prgrms.p2p.domain.place.dto.SearchPlaceRequest;
import com.prgrms.p2p.domain.place.entity.Category;
import com.prgrms.p2p.domain.place.entity.Place;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;

public class PlaceSearchRepositoryImpl implements PlaceSearchRepository {

  private final JPAQueryFactory jpaQueryFactory;

  public PlaceSearchRepositoryImpl(EntityManager em) {
    this.jpaQueryFactory = new JPAQueryFactory(em);
  }

  @Override
  public Slice<Place> searchPlace(SearchPlaceRequest request, Pageable pageable) {
    JPAQuery<Place> placeJPAQuery = jpaQueryFactory.selectFrom(place)
        .where(
            keywordListContains(request.getKeyword()),
            categoryEq(request.getCategory())
        )
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize() + 1);

    for (Sort.Order o : pageable.getSort()) {
      PathBuilder pathBuilder = new PathBuilder<>(place.getType(), place.getMetadata());

      placeJPAQuery.orderBy(new OrderSpecifier(
          o.isAscending() ? Order.ASC : Order.DESC, pathBuilder.get(o.getProperty())
          )
      );
    }

    List<Place> placeList = placeJPAQuery.fetch();

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