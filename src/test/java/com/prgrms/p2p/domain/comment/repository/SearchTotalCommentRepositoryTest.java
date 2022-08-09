package com.prgrms.p2p.domain.comment.repository;

import static com.prgrms.p2p.domain.comment.util.CommentConverter.toMergeCommentResponse;

import com.prgrms.p2p.domain.comment.dto.MergeCommentResponse;
import com.prgrms.p2p.domain.comment.entity.CourseComment;
import com.prgrms.p2p.domain.comment.entity.PlaceComment;
import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.course.entity.Period;
import com.prgrms.p2p.domain.course.entity.Region;
import com.prgrms.p2p.domain.course.entity.Spot;
import com.prgrms.p2p.domain.course.entity.Theme;
import com.prgrms.p2p.domain.course.repository.CourseRepository;
import com.prgrms.p2p.domain.place.entity.Address;
import com.prgrms.p2p.domain.place.entity.Category;
import com.prgrms.p2p.domain.place.entity.PhoneNumber;
import com.prgrms.p2p.domain.place.entity.Place;
import com.prgrms.p2p.domain.place.repository.PlaceRepository;
import com.prgrms.p2p.domain.user.entity.Sex;
import com.prgrms.p2p.domain.user.entity.User;
import com.prgrms.p2p.domain.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
class SearchTotalCommentRepositoryTest {

  @Autowired
  UserRepository userRepository;

  @Autowired
  CourseRepository courseRepository;

  @Autowired
  CourseCommentRepository courseCommentRepository;

  @Autowired
  PlaceRepository placeRepository;

  @Autowired
  PlaceCommentRepository placeCommentRepository;

  @Test
  @DisplayName("입력 순서에 상관 없이 대댓글은 댓글의 하위에, 댓글은 작성 순서대로 나타납니다.")
  public void findCourseComment() throws Exception {

    //course
    String title = "title";
    Period oneDay = Period.ONE_DAY;
    Region region = Region.경기;
    String description = "description";
    Set<Theme> themes = null;
    Set<Spot> spots = null;
    String email = "e@mail.com";
    String password = "password";
    String nick = "nick";
    String birth = "2010-01-01";
    Sex male = Sex.MALE;

    User user = userRepository.save(new User(email, password, nick, birth, male));
    Long userId = user.getId();

    Course course = courseRepository.save(
        new Course(title, oneDay, region, themes, spots, user));

    Place place = placeRepository.save(
        new Place("kakao", "post title", new Address("A", "b"), "kla", "lo", Category.AC5,
            new PhoneNumber("010-111-1111")));

    placeRepository.save(
        new Place("kakao", "post title", new Address("A", "b"), "kla", "lo", Category.AC5,
            new PhoneNumber("010-111-1111")));

    //comment
    Long commentId = courseCommentRepository.save(
        new CourseComment("comment1", null, userId, course)).getId();

    // 1번에 다는 대댓글
    courseCommentRepository.save(
        new CourseComment("    comment2", commentId, userId, course));
    courseCommentRepository.save(
        new CourseComment("    comment3", commentId, userId, course));

    Long commentId2 = courseCommentRepository.save(
        new CourseComment("comment8", null, userId, course)).getId();

    PlaceComment placeComment1 = placeCommentRepository.save(
        new PlaceComment("place comment 1 입니다.", null, userId, place));
    // 2번에 다는 대댓글
    courseCommentRepository.save(
        new CourseComment("    comment9", commentId2, userId, course));

    courseCommentRepository.save(
        new CourseComment("    comment10", commentId2, userId, course));
    courseCommentRepository.save(
        new CourseComment("    comment11", commentId2, userId, course));
    courseCommentRepository.save(
        new CourseComment("    comment12", commentId2, userId, course));

    PlaceComment placeComment2 = placeCommentRepository.save(
        new PlaceComment("place comment 2 입니다.", null, userId, place));

    // 1번에 다는 대댓글
    courseCommentRepository.save(
        new CourseComment("    comment4", commentId, userId, course));
    courseCommentRepository.save(
        new CourseComment("    comment5", commentId, userId, course));
    courseCommentRepository.save(
        new CourseComment("    comment6", commentId, userId, course));
    courseCommentRepository.save(
        new CourseComment("    comment7", commentId, userId, course));

    PlaceComment placeComment3 = placeCommentRepository.save(
        new PlaceComment("place comment 3 입니다.", null, userId, place));

    // 2번에 다는 대댓글
    courseCommentRepository.save(
        new CourseComment("    comment13", commentId2, userId, course));
    courseCommentRepository.save(
        new CourseComment("    comment14", commentId2, userId, course));
    courseCommentRepository.save(
        new CourseComment("    comment15", commentId2, userId, course));

    PlaceComment placeComment4 = placeCommentRepository.save(
        new PlaceComment("place comment 4 입니다.", null, userId, place));

    // then
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

//    mergeCommentsByUser.
    for (MergeCommentResponse mergeCommentResponse : mergeCommentsByUser) {
      System.out.println(
          "mergeCommentResponse.getCreatedAt() = " + mergeCommentResponse.getCreatedAt());
      System.out.println(
          "mergeCommentResponse.getComment() = " + mergeCommentResponse.getComment());
      System.out.println("mergeCommentResponse.getSubCommentCount() = "
          + mergeCommentResponse.getSubCommentCount());
      System.out.println(
          "mergeCommentResponse.getContent().getTitle() = " + mergeCommentResponse.getContent()
              .getTitle());
      System.out.println(
          "mergeCommentResponse.getContent().getType() = " + mergeCommentResponse.getContent()
              .getType());
    }
  }
}