package com.prgrms.p2p.domain.comment.service;


import com.prgrms.p2p.domain.comment.dto.MergeCommentResponse;
import com.prgrms.p2p.domain.comment.entity.CourseComment;
import com.prgrms.p2p.domain.comment.entity.PlaceComment;
import com.prgrms.p2p.domain.comment.repository.CourseCommentRepository;
import com.prgrms.p2p.domain.comment.repository.PlaceCommentRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MergeCommentServiceTest {

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

  @Autowired
  MergeCommentService mergeCommentService;

  @Test
  @DisplayName("머지 코멘트 서비스 테스트.")
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

    PageRequest pageable = PageRequest.of(1, 13);

    System.out.println(pageable.getOffset());
    // then
    Slice<MergeCommentResponse> commentsByUserId = mergeCommentService.findCommentsByUserId(
        userId, pageable);


//    for (MergeCommentResponse mergeCommentResponse : commentsByUserId) {
//      String comment = mergeCommentResponse.getComment();
//      System.out.println("comment = " + comment);
//    }
    long offset = commentsByUserId.getPageable().getOffset();
    System.out.println("offset = " + offset);

    List<MergeCommentResponse> content = commentsByUserId.getContent();
    for (MergeCommentResponse mergeCommentResponse : content) {
      System.out.println(
          "mergeCommentResponse.getComment() = " + mergeCommentResponse.getComment());
    }
    System.out.println("=========================================================");
    int size = commentsByUserId.getSize();
    System.out.println("size = " + size);
    Pageable pageable1 = commentsByUserId.nextPageable();
    System.out.println("pageable1 = " + pageable1);
    boolean b = commentsByUserId.hasNext();
    System.out.println("b = " + b);
    boolean first = commentsByUserId.isFirst();
    System.out.println("first = " + first);
    boolean last = commentsByUserId.isLast();
    System.out.println("last = " + last);


  }
}