package com.prgrms.p2p.domain.place.controller;

import com.prgrms.p2p.domain.common.exception.UnAuthorizedException;
import com.prgrms.p2p.domain.course.dto.CoursePlaceRequest;
import com.prgrms.p2p.domain.course.entity.Region;
import com.prgrms.p2p.domain.course.entity.Sorting;
import com.prgrms.p2p.domain.place.dto.DetailPlaceResponse;
import com.prgrms.p2p.domain.place.dto.SearchPlaceDto;
import com.prgrms.p2p.domain.place.dto.SummaryPlaceResponse;
import com.prgrms.p2p.domain.place.service.PlaceService;
import com.prgrms.p2p.domain.place.util.PlaceConverter;
import com.prgrms.p2p.domain.user.aop.annotation.CurrentUser;
import com.prgrms.p2p.domain.user.pojo.CustomUserDetails;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/places")
public class PlaceController {

  private final PlaceService placeService;

  @PostMapping
  public ResponseEntity<Long> savePlace(@RequestBody CoursePlaceRequest coursePlaceRequest,
      @CurrentUser CustomUserDetails user) {
    validateLoginUser(user);
    Long placeId = placeService.save(coursePlaceRequest).getId();
    return ResponseEntity.ok(placeId);
  }

  @GetMapping("/{placeId}")
  public ResponseEntity<DetailPlaceResponse> getDetailPlace(@PathVariable("placeId") Long placeId,
      @CurrentUser CustomUserDetails user) {
    Long userId = Objects.isNull(user) ? null : user.getId();
    DetailPlaceResponse response = placeService.findDetail(placeId, Optional.ofNullable(userId));
    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<Slice<SummaryPlaceResponse>> getSummaryPlaceList(
      @RequestParam("keyword") Optional<String> keyword,
      @RequestParam("region") Optional<Region> region,
      @RequestParam("sorting") Optional<Sorting> sorting,
      @PageableDefault(page = 0, size = 15) Pageable pageable,
      @CurrentUser CustomUserDetails user) {
    Long userId = Objects.isNull(user) ? null : user.getId();

    SearchPlaceDto searchPlaceDto = PlaceConverter.toSearchPlaceDto(keyword, region, sorting);
    Slice<SummaryPlaceResponse> summaryList =
        placeService.findSummaryList(searchPlaceDto, pageable, userId);

    return ResponseEntity.ok(summaryList);
  }

  @GetMapping("/bookmark")
  public ResponseEntity<Slice<SummaryPlaceResponse>> getBookmarkPlaceList(
      @RequestParam("userId") Long targetUserId, Pageable pageable,
      @CurrentUser CustomUserDetails user) {
    Long userId = Objects.isNull(user) ? null : user.getId();
    Slice<SummaryPlaceResponse> bookmarkedPlaceList = placeService.findBookmarkedPlaceList(userId,
        targetUserId, pageable);
    return ResponseEntity.ok(bookmarkedPlaceList);
  }

  private void validateLoginUser(CustomUserDetails user) {
    if (Objects.isNull(user)) {
      throw new UnAuthorizedException("로그인이 필요합니다.");
    }
  }
}