package com.prgrms.p2p.domain.place.controller;

import com.prgrms.p2p.domain.place.dto.DetailPlaceResponse;
import com.prgrms.p2p.domain.place.dto.SearchPlaceRequest;
import com.prgrms.p2p.domain.place.dto.SummaryPlaceResponse;
import com.prgrms.p2p.domain.place.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/places")
public class PlaceController {

  private final PlaceService placeService;

  @GetMapping("/{placeId}")
  public ResponseEntity<DetailPlaceResponse> getDetailPlace(@PathVariable("placeId") Long placeId) {
    DetailPlaceResponse response = placeService.findDetail(placeId);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/")
  public ResponseEntity<Slice<SummaryPlaceResponse>> getSummaryPlaceList(
      SearchPlaceRequest searchPlaceRequest, Pageable pageable) {
    Slice<SummaryPlaceResponse> summaryList =
        placeService.findSummaryList(searchPlaceRequest, pageable);

    return ResponseEntity.ok(summaryList);
  }
}