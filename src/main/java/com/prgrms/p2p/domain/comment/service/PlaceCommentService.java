package com.prgrms.p2p.domain.comment.service;

import static com.prgrms.p2p.domain.comment.util.CommentConverter.toPlaceComment;

import com.prgrms.p2p.domain.comment.dto.CreateCommentRequest;
import com.prgrms.p2p.domain.comment.dto.UpdateCommentRequest;
import com.prgrms.p2p.domain.comment.entity.PlaceComment;
import com.prgrms.p2p.domain.comment.entity.Visibility;
import com.prgrms.p2p.domain.comment.repository.PlaceCommentRepository;
import com.prgrms.p2p.domain.common.exception.BadRequestException;
import com.prgrms.p2p.domain.common.exception.NotFoundException;
import com.prgrms.p2p.domain.common.exception.UnAuthorizedException;
import com.prgrms.p2p.domain.place.entity.Place;
import com.prgrms.p2p.domain.place.repository.PlaceRepository;
import com.prgrms.p2p.domain.user.entity.User;
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


  public Long save(CreateCommentRequest createCommentRequest, Long placeId, Long userId) {
    validateAuth(!userRepository.existsById(userId), "존재하지 않는 계정입니다.");

    Place place = placeRepository.findById(placeId)
        .orElseThrow(() -> new NotFoundException("장소가 존재하지 않습니다."));

    Long rootCommentId = createCommentRequest.getRootCommentId();

    if (!Objects.isNull(rootCommentId)) {
      PlaceComment parentComment = placeCommentRepository.findById(rootCommentId)
          .orElseThrow(() -> new NotFoundException("존재하지 않는 댓글에 하위 댓글을 작성할 수 없습니다."));

      if (!Objects.isNull(parentComment.getRootCommentId())) {
        throw new BadRequestException("대댓글에 대댓글을 작성할 수 없습니다.");
      }
    }

    PlaceComment placeComment = toPlaceComment(createCommentRequest, place, userId);
    return placeCommentRepository.save(placeComment).getId();
  }

  public Long updatePlaceComment(UpdateCommentRequest updateCommentRequest, Long placeId,
      Long placeCommentId, Long userId) {

    Place place = placeRepository.findById(placeId)
        .orElseThrow(() -> new NotFoundException("존재하지 않는 장소입니다."));
    User user = userRepository.findById(userId)
        .orElseThrow(RuntimeException::new);

    PlaceComment placeComment = placeCommentRepository.findById(placeCommentId)
        .orElseThrow(() -> new NotFoundException("존재하지 않는 댓글입니다."));

    if(!placeComment.getPlace().equals(place)){
      throw new NotFoundException("해당 장소에 존재하지 않는 댓글 입니다.");
    }

    validateAuth(!placeComment.getAuthForUpdate(user), "댓글의 수정 권한이 없습니다.");

    placeComment.changeComment(updateCommentRequest.getComment());
    return placeComment.getId();
  }

  public void deletePlaceComment(Long placeId, Long placeCommentId, Long userId) {

    Place place = placeRepository.findById(placeId)
        .orElseThrow(() -> new NotFoundException("존재하지 않는 장소입니다."));

    User user = userRepository.findById(userId)
        .orElseThrow(RuntimeException::new);

    PlaceComment placeComment = placeCommentRepository.findById(placeCommentId)
        .orElseThrow(() -> new NotFoundException("존재하지 않는 댓글입니다."));

    if(!placeComment.getPlace().equals(place)){
      throw new NotFoundException("해당 장소에 존재하지 않는 댓글 입니다.");
    }

    if (!placeComment.getVisibility().equals(Visibility.TRUE)) {
      throw new BadRequestException("이미 삭제된 댓글은 삭제할 수 없습니다.");
    }

    validateAuth(!placeComment.getAuthForDelete(user), "댓글의 삭제 권한이 없습니다.");

    if (!Objects.isNull(placeComment.getRootCommentId())) {
      checkDeleteParentComment(placeComment);
      placeComment.changeVisibility(Visibility.FALSE);
      return;
    }

    if (placeCommentRepository.findSubCommentCount(placeComment.getId()) == 0) {
      placeComment.changeVisibility(Visibility.FALSE);
      return;
    }

    placeComment.changeVisibility(Visibility.DELETED_INFORMATION);
  }

  private void checkDeleteParentComment(PlaceComment placeComment) {
    PlaceComment parentComment = placeCommentRepository.findById(placeComment.getRootCommentId())
        .orElseThrow(() -> new NotFoundException("부모 댓글이 존재하지 않습니다"));

    if (placeCommentRepository.findSubCommentCount(placeComment.getRootCommentId()) == 1
        && parentComment.getVisibility().equals(Visibility.DELETED_INFORMATION)) {
      parentComment.changeVisibility(Visibility.FALSE);
    }
  }

  private void validateAuth(boolean courseComment, String message) {
    if (courseComment) {
      throw new UnAuthorizedException(message);
    }
  }
}
