package com.prgrms.p2p.domain.comment.service;

import static com.prgrms.p2p.domain.comment.util.CommentConverter.*;

import com.prgrms.p2p.domain.comment.dto.MergeCommentResponse;
import com.prgrms.p2p.domain.comment.repository.CourseCommentRepository;
import com.prgrms.p2p.domain.comment.repository.PlaceCommentRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MergeCommentService {

  private final CourseCommentRepository courseCommentRepository;
  private final PlaceCommentRepository placeCommentRepository;

  public SliceImpl<MergeCommentResponse> findCommentsByUserId(Long userId, Pageable pageable) {
    List<MergeCommentResponse> mergeCommentsByUser = new ArrayList<>();
    List<MergeCommentResponse> courseComments = courseCommentRepository.findCourseCommentsByUserId(
            userId).stream().map(comment -> toMergeCommentResponse(comment, "코스"))
        .collect(Collectors.toList());
    List<MergeCommentResponse> placeComments = placeCommentRepository.findPlaceCommentsByUserId(
            userId).stream().map(comment -> toMergeCommentResponse(comment, "장소"))
        .collect(Collectors.toList());

    mergeCommentsByUser.addAll(courseComments);
    mergeCommentsByUser.addAll(placeComments);

    mergeCommentsByUser.sort(Collections.reverseOrder());
    boolean hasNext = false;

    if (mergeCommentsByUser.size() > pageable.getPageSize()) {
      mergeCommentsByUser.remove(pageable.getPageSize());
      hasNext = true;
    }

    return new SliceImpl<>(mergeCommentsByUser, pageable, hasNext);
  }
}