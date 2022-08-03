package com.prgrms.p2p.domain.place.controller;

import com.prgrms.p2p.domain.place.dto.DetailPlaceResponse;
import com.prgrms.p2p.domain.place.dto.RecordRequest;
import com.prgrms.p2p.domain.place.dto.SearchPlaceRequest;
import com.prgrms.p2p.domain.place.dto.SummaryPlaceResponse;
import com.prgrms.p2p.domain.place.service.PlaceService;
import com.prgrms.p2p.domain.user.aop.annotation.CurrentUser;
import com.prgrms.p2p.domain.user.pojo.CustomUserDetails;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/places")
public class PlaceController {

  private final PlaceService placeService;

  @GetMapping("/{placeId}")
  public ResponseEntity<DetailPlaceResponse> getDetailPlace(
      @PathVariable("placeId") Long placeId,
      @CurrentUser CustomUserDetails user) {
    Long userId = Objects.isNull(user) ? null : user.getId();
    DetailPlaceResponse response = placeService.findDetail(placeId, Optional.ofNullable(userId));
    return ResponseEntity.ok(response);
  }

  @GetMapping("/")
  public ResponseEntity<Slice<SummaryPlaceResponse>> getSummaryPlaceList(
      @RequestBody SearchPlaceRequest searchPlaceRequest,
      Pageable pageable,
      @CurrentUser CustomUserDetails user) {
    Long userId = Objects.isNull(user) ? null : user.getId();
    Slice<SummaryPlaceResponse> summaryList =
        placeService.findSummaryList(searchPlaceRequest, pageable, userId);

    return ResponseEntity.ok(summaryList);
  }

  @GetMapping("/bookmark")
  public ResponseEntity<Slice<SummaryPlaceResponse>> getBookmarkPlaceList(
      Pageable pageable,
      @RequestBody RecordRequest recordRequest) {
    Slice<SummaryPlaceResponse> bookmarkedPlaceList
        = placeService.findBookmarkedPlaceList(recordRequest, pageable);
    return ResponseEntity.ok(bookmarkedPlaceList);
  }
}