package com.prgrms.p2p.domain.place.controller;

import com.prgrms.p2p.domain.common.exception.BadRequestException;
import com.prgrms.p2p.domain.common.exception.UnAuthorizedException;
import com.prgrms.p2p.domain.course.dto.CoursePlaceRequest;
import com.prgrms.p2p.domain.course.entity.Region;
import com.prgrms.p2p.domain.course.entity.Sorting;
import com.prgrms.p2p.domain.place.dto.DetailPlaceResponse;
import com.prgrms.p2p.domain.place.dto.SearchPlaceDto;
import com.prgrms.p2p.domain.place.dto.SummaryPlaceResponse;
import com.prgrms.p2p.domain.place.service.PlaceService;
import com.prgrms.p2p.domain.place.util.PlaceConverter;
import com.prgrms.p2p.domain.user.aop.annotation.Auth;
import com.prgrms.p2p.domain.user.aop.annotation.CurrentUser;
import com.prgrms.p2p.domain.user.pojo.CustomUserDetails;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
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

  @Operation(summary = "장소 등록", description = "인증된 사용자는 장소를 등록할 수 있습니다.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "댓글 등록 성공"),
      @ApiResponse(code = 401, message = "인증 받지 않은 사용자 or 댓글 수정 권한 없음"),
      @ApiResponse(code = 500, message = "옳바르지 않은 입력 형식에 대한 Json 오류")
  })
  @Auth
  @PostMapping
  public ResponseEntity<Long> savePlace(@RequestBody CoursePlaceRequest coursePlaceRequest,
      @CurrentUser CustomUserDetails user) {
    validateLoginUser(user);
    Long placeId = placeService.saveWithoutKakaoMapId(coursePlaceRequest).getId();
    return ResponseEntity.ok(placeId);
  }

  @Operation(summary = "장소 상세 조회", description = "누구나 장소의 상세 정보를 조회할 수 있습니다..")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "장소 상세 조회 성공"),
      @ApiResponse(code = 404, message = "장소가 존재하지 않습니다.")
  })
  @GetMapping("/{placeId}")
  public ResponseEntity<DetailPlaceResponse> getDetailPlace(@PathVariable("placeId") Long placeId,
      @CurrentUser CustomUserDetails user) {
    Long userId = Objects.isNull(user) ? null : user.getId();
    DetailPlaceResponse response = placeService.findDetail(placeId, Optional.ofNullable(userId));
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "장소 목록 조회", description = "누구나 장소의 목록을 조건별로 조회할 수 있습니다..")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "장소 목록 조회 성공"),
      @ApiResponse(code = 500, message = "잘못된 검색 조건 입력")
  })
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

  @Operation(summary = "자신 혹은 타인의 북마크한 장소 목록 조회",
      description = "북마크한 장소를 조회합니다. 파라미터가 있을 경우 그 유저를 없으면 로그인되어있는 유저의 북마크 목록을 조호합니다.")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "자신 혹은 타인의 북마크한 장소 목록 성공"),
      @ApiResponse(code = 400, message = "잘못된 요청(로그인 or 조회하고싶은 유저의 아이디가 필요)"),
  })
  @GetMapping("/bookmark")
  public ResponseEntity<Slice<SummaryPlaceResponse>> getBookmarkPlaceList(
      @RequestParam("userId") Optional<Long> userId,
      @PageableDefault(page = 0, size = 15) Pageable pageable,
      @CurrentUser CustomUserDetails user) {
    if (Objects.isNull(user) && userId.isEmpty()) {
      throw new BadRequestException("잘못된 요청(로그인 or 조회하고싶은 유저의 아이디가 필요)");
    }
    Long loginUserId = Objects.isNull(user) ? null : user.getId();
    Long targetUserId = userId.isEmpty() ? user.getId() : userId.get();

    Slice<SummaryPlaceResponse> bookmarkedPlaceList
        = placeService.findBookmarkedPlaceList(loginUserId, targetUserId, pageable);
    return ResponseEntity.ok(bookmarkedPlaceList);
  }

  private void validateLoginUser(CustomUserDetails user) {
    if (Objects.isNull(user)) {
      throw new UnAuthorizedException("로그인이 필요합니다.");
    }
  }
}