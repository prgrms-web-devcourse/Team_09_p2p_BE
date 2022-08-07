package com.prgrms.p2p.domain.course.controller;

import com.prgrms.p2p.domain.course.dto.CreateCourseRequest;
import com.prgrms.p2p.domain.course.dto.DetailCourseResponse;
import com.prgrms.p2p.domain.course.dto.SearchCourseRequest;
import com.prgrms.p2p.domain.course.dto.SummaryCourseResponse;
import com.prgrms.p2p.domain.course.entity.Period;
import com.prgrms.p2p.domain.course.entity.Region;
import com.prgrms.p2p.domain.course.entity.Sorting;
import com.prgrms.p2p.domain.course.entity.Spot;
import com.prgrms.p2p.domain.course.entity.Theme;
import com.prgrms.p2p.domain.course.service.CourseQueryService;
import com.prgrms.p2p.domain.course.service.CourseService;
import com.prgrms.p2p.domain.user.aop.annotation.CurrentUser;
import com.prgrms.p2p.domain.user.pojo.CustomUserDetails;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

  @PostMapping
  public ResponseEntity<Long> register(
      @RequestPart("course") CreateCourseRequest createCourseRequest,
      @RequestPart("images") List<MultipartFile> images, @CurrentUser CustomUserDetails user) {
    Long userId = Objects.isNull(user) ? null : user.getId();
    Long courseId = courseService.save(createCourseRequest, images, userId);
    return ResponseEntity.created(URI.create("/" + courseId)).body(courseId);
  }

  @GetMapping("/{courseId}")
  public ResponseEntity<DetailCourseResponse> getDetailCourse(
      @PathVariable("courseId") Long courseId, @CurrentUser CustomUserDetails user) {
    Long userId = Objects.isNull(user) ? null : user.getId();
    DetailCourseResponse response = courseQueryService.findDetail(courseId, userId);
    return ResponseEntity.ok(response);
  }

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

  @GetMapping("/bookmark")
  public ResponseEntity<Slice<SummaryCourseResponse>> getBookmarkPlaceList(
      @RequestParam("userId") Long userId, Pageable pageable) {
    Slice<SummaryCourseResponse> bookmarkedPlaceList = courseQueryService.findBookmarkedCourseList(
        pageable, userId);

    return ResponseEntity.ok(bookmarkedPlaceList);
  }
}