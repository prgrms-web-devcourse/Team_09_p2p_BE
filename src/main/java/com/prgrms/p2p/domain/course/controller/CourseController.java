package com.prgrms.p2p.domain.course.controller;

import com.prgrms.p2p.domain.common.exception.UnAuthorizedException;
import com.prgrms.p2p.domain.course.dto.CreateCourseRequest;
import com.prgrms.p2p.domain.course.dto.DetailCourseResponse;
import com.prgrms.p2p.domain.course.dto.SearchCourseRequest;
import com.prgrms.p2p.domain.course.dto.SummaryCourseResponse;
import com.prgrms.p2p.domain.course.dto.UpdateCourseRequest;
import com.prgrms.p2p.domain.course.entity.Period;
import com.prgrms.p2p.domain.course.entity.Region;
import com.prgrms.p2p.domain.course.entity.Sorting;
import com.prgrms.p2p.domain.course.entity.Spot;
import com.prgrms.p2p.domain.course.entity.Theme;
import com.prgrms.p2p.domain.course.service.CourseQueryService;
import com.prgrms.p2p.domain.course.service.CourseService;
import com.prgrms.p2p.domain.user.aop.annotation.CurrentUser;
import com.prgrms.p2p.domain.user.pojo.CustomUserDetails;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/courses")
public class CourseController {

  private final CourseService courseService;
  private final CourseQueryService courseQueryService;

  @Operation(summary = "코스 등록", description = "인증된 사용자는 코스를 등록할 수 있습니다.")
  @ApiResponses(value = {@ApiResponse(code = 201, message = "코스 등록 성공"),
      @ApiResponse(code = 401, message = "인증 받지 않은 사용자 권한 없음"),
      @ApiResponse(code = 404, message = "존재하지 않는 유저인 경우"),
      @ApiResponse(code = 500, message = "옳바르지 않은 입력 형식에 대한 Json 오류 또는 S3 업로드 오류인 경우")})
  @PostMapping
  public ResponseEntity<Long> register(
      @RequestPart("course") CreateCourseRequest createCourseRequest,
      @RequestPart("images") List<MultipartFile> images, @CurrentUser CustomUserDetails user) {
    validateLoginUser(user);
    Long courseId = courseService.save(createCourseRequest, images, user.getId());
    return ResponseEntity.created(URI.create("/" + courseId)).body(courseId);
  }

  @Operation(summary = "코스 상세", description = "모든 사용자는 코스 상세 조회를 할 수 있습니다.")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "코스 조회 성공"),
      @ApiResponse(code = 401, message = "인증 받지 않은 사용자 권한 없음"),
      @ApiResponse(code = 404, message = "존재하지 않는 코스인 경우")})
  @GetMapping("/{courseId}")
  public ResponseEntity<DetailCourseResponse> getDetailCourse(
      @PathVariable("courseId") Long courseId, @CurrentUser CustomUserDetails user) {
    Long userId = Objects.isNull(user) ? null : user.getId();
    DetailCourseResponse response = courseQueryService.findDetail(courseId, userId);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "코스 목록", description = "모든 사용자는 코스 목록을 조회를 할 수 있습니다.")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "코스 조회 성공"),
      @ApiResponse(code = 401, message = "인증 받지 않은 사용자 권한 없음"),})
  @GetMapping
  public ResponseEntity<Slice<SummaryCourseResponse>> getSummaryCourseList(
      @RequestParam(required = false) String keyword, @RequestParam(required = false) Region region,
      @RequestParam(required = false) String period, @RequestParam(required = false) Long placeId,
      @RequestParam(required = false) List<Spot> spots,
      @RequestParam(required = false) List<Theme> themes,
      @RequestParam(required = false) Sorting sorting,
      @PageableDefault(page = 0, size = 15) Pageable pageable,
      @CurrentUser CustomUserDetails user) {

    Long userId = Objects.isNull(user) ? null : user.getId();
    SearchCourseRequest searchCourseRequest = SearchCourseRequest.builder().keyword(keyword)
        .region(region).period(Period.from(period)).placeId(placeId).spots(spots).themes(themes)
        .sorting(sorting).build();

    Slice<SummaryCourseResponse> summaryList = courseQueryService.findSummaryList(
        searchCourseRequest, pageable, userId);
    return ResponseEntity.ok(summaryList);
  }

  @Operation(summary = "유저의 북마크한 코스 목록", description = "인증된 사용자는 북마크한 코스 목록을 조회를 할 수 있습니다.")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "코스 조회 성공"),
      @ApiResponse(code = 401, message = "인증 받지 않은 사용자 권한 없음"),})
  @GetMapping("/bookmark")
  public ResponseEntity<Slice<SummaryCourseResponse>> getBookmarkPlaceList(
      @RequestParam("userId") Long targetUserId, @PageableDefault(page = 0, size = 15) Pageable pageable,
      @CurrentUser CustomUserDetails user) {
    Long loginId = Objects.isNull(user) ? null : user.getId();
    Slice<SummaryCourseResponse> bookmarkedPlaceList = courseQueryService.findBookmarkedCourseList(
        pageable, targetUserId, loginId);

    return ResponseEntity.ok(bookmarkedPlaceList);
  }

  @Operation(summary = "유저의  코스 목록", description = "인증된 사용자는 자신이 등록한 코스 목록을 조회를 할 수 있습니다.")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "코스 조회 성공"),
      @ApiResponse(code = 401, message = "인증 받지 않은 사용자 권한 없음"),})
  @GetMapping("/users")
  public ResponseEntity<Slice<SummaryCourseResponse>> getMyCoursesList(
      @PageableDefault(page = 0, size = 15) Pageable pageable,
      @CurrentUser CustomUserDetails user) {
    validateLoginUser(user);
    Slice<SummaryCourseResponse> bookmarkedPlaceList = courseQueryService.findMyCourseList(pageable,
        user.getId());

    return ResponseEntity.ok(bookmarkedPlaceList);
  }

  @Operation(summary = "타 유저의 코스 목록", description = "모든 사용자는 타인이 등록한 코스 목록을 조회를 할 수 있습니다.")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "코스 조회 성공")})
  @GetMapping("/users/{userId}")
  public ResponseEntity<Slice<SummaryCourseResponse>> getOtherCoursesList(
      @PathVariable("userId") Long otherUserId,
      @PageableDefault(page = 0, size = 15) Pageable pageable,
      @CurrentUser CustomUserDetails user) {
    Long userId = Objects.isNull(user) ? null : user.getId();
    Slice<SummaryCourseResponse> bookmarkedPlaceList = courseQueryService.findOtherCourseList(
        pageable, userId, otherUserId);

    return ResponseEntity.ok(bookmarkedPlaceList);
  }

  @Operation(summary = "코스 수정", description = "인증된 사용자는 코스를 수정할 수 있습니다.")
  @ApiResponses(value = {@ApiResponse(code = 201, message = "코스 수정 성공"),
      @ApiResponse(code = 401, message = "인증 받지 않은 사용자 권한 없음"),
      @ApiResponse(code = 404, message = "존재하지 않는 유저 또는 코스인 경우"),
      @ApiResponse(code = 500, message = "옳바르지 않은 입력 형식에 대한 Json 오류 또는 S3 업로드 오류인 경우")})
  @PutMapping("/{courseId}")
  public ResponseEntity<Long> modify(@PathVariable("courseId") Long courseId,
      @RequestPart("course") UpdateCourseRequest updateCourseRequest,
      @RequestPart("images") List<MultipartFile> images, @CurrentUser CustomUserDetails user) {
    validateLoginUser(user);
    Long id = courseService.modify(courseId, updateCourseRequest, images, user.getId());
    return ResponseEntity.created(URI.create("/" + id)).body(courseId);
  }

  @Operation(summary = "코스 삭제", description = "인증된 사용자는 코스를 삭제할 수 있습니다.")
  @ApiResponses(value = {@ApiResponse(code = 204, message = "코스 삭제 성공"),
      @ApiResponse(code = 401, message = "인증 받지 않은 사용자 권한 없음")})
  @DeleteMapping("/{courseId}")
  public ResponseEntity deleteCourse(@PathVariable("courseId") Long courseId,
      @CurrentUser CustomUserDetails user) {
    Long userId = Objects.isNull(user) ? null : user.getId();
    courseService.deleteCourse(courseId, userId);
    return ResponseEntity.noContent().build();
  }

  private void validateLoginUser(CustomUserDetails user) {
    if (Objects.isNull(user)) {
      throw new UnAuthorizedException("로그인이 필요합니다.");
    }
  }
}