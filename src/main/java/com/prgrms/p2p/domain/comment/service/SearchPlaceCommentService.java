package com.prgrms.p2p.domain.comment.service;

import static com.prgrms.p2p.domain.comment.util.CommentConverter.toPlaceCommentResponse;

import com.prgrms.p2p.domain.comment.dto.PlaceCommentForQueryDsl;
import com.prgrms.p2p.domain.comment.dto.PlaceCommentResponse;
import com.prgrms.p2p.domain.comment.repository.PlaceCommentRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class SearchPlaceCommentService {

  private final PlaceCommentRepository placeCommentRepository;

  public SearchPlaceCommentService(
      PlaceCommentRepository placeCommentRepository) {
    this.placeCommentRepository = placeCommentRepository;
  }

  public PlaceCommentResponse findPlaceComment(Long placeId){
    List<PlaceCommentForQueryDsl> placeComments = placeCommentRepository.findPlaceComments(placeId);

    return toPlaceCommentResponse(placeComments, placeId);
  }


}
