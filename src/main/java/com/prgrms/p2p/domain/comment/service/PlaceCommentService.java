package com.prgrms.p2p.domain.comment.service;

import static com.prgrms.p2p.domain.comment.util.CommentConverter.toPlaceComment;

import com.prgrms.p2p.domain.comment.dto.CreateCommentRequest;
import com.prgrms.p2p.domain.comment.entity.PlaceComment;
import com.prgrms.p2p.domain.comment.repository.PlaceCommentRepository;
import com.prgrms.p2p.domain.common.exception.NotFoundException;
import com.prgrms.p2p.domain.common.exception.UnAuthorizedException;
import com.prgrms.p2p.domain.place.entity.Place;
import com.prgrms.p2p.domain.place.repository.PlaceRepository;
import com.prgrms.p2p.domain.user.repository.UserRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class PlaceCommentService {

  private final PlaceCommentRepository placeCommentRepository;
  private final PlaceRepository placeRepository;
  private final UserRepository userRepository;


  public Long save(CreateCommentRequest commentRequest, Long placeId, Long userId) {
    //유저가 존재하는지 확인
    validateAuth(!userRepository.existsById(userId), "존재하지 않는 계정입니다.");

    //Place 가져오기
    Place place = placeRepository.findById(placeId)
        .orElseThrow(() -> new NotFoundException("장소가 존재하지 않습니다"));

    //상위 댓글이 있는지 확인
    Long rootCommentId = commentRequest.getRootCommentId();
    validateAuth(
        !Objects.isNull(rootCommentId) && !placeCommentRepository.existsById(rootCommentId),
        "존재하지 않는 댓글에 하위 댓글을 작성할 수 없습니다.");

    //placeComment으로 바꾸기
    PlaceComment placeComment = toPlaceComment(commentRequest, place, userId);

    return placeCommentRepository.save(placeComment).getId();
  }

  private void validateAuth(boolean courseComment, String message) {
    if (courseComment) {
      throw new UnAuthorizedException(message);
    }
  }
}
