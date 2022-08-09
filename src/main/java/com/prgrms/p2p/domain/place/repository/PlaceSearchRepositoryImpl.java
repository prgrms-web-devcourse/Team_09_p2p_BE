package com.prgrms.p2p.domain.place.repository;

import static com.prgrms.p2p.domain.place.entity.QPlace.place;

import com.prgrms.p2p.domain.course.entity.Region;
import com.prgrms.p2p.domain.course.entity.Sorting;
import com.prgrms.p2p.domain.place.dto.SearchPlaceDto;
import com.prgrms.p2p.domain.place.entity.Place;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.util.ObjectUtils;

public class PlaceSearchRepositoryImpl implements PlaceSearchRepository {

  private final JPAQueryFactory jpaQueryFactory;

  public PlaceSearchRepositoryImpl(EntityManager em) {
    this.jpaQueryFactory = new JPAQueryFactory(em);
  }

  @Override
  public Slice<Place> searchPlace(SearchPlaceDto searchPlaceDto, Pageable pageable) {
    JPAQuery<Place> placeJPAQuery = jpaQueryFactory.selectFrom(place)
        .where(
            keywordListContains(searchPlaceDto.getKeyword().orElse("")),
            regionEq(searchPlaceDto.getRegion()),
            place.kakaoMapId.isNotNull()
        )
        .orderBy(sortingEq(searchPlaceDto.getSorting()))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize() + 1);

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

  private BooleanExpression regionEq(Optional<Region> region) {
    return region.isEmpty() ? null : place.coursePlaces.any().course.region.eq(region.get());
  }

  private OrderSpecifier<?> sortingEq(Optional<Sorting> sorting) {
    return sorting.isEmpty() ? Sorting.최신순.expressionForPlace() : sorting.get().expressionForPlace();
  }
}