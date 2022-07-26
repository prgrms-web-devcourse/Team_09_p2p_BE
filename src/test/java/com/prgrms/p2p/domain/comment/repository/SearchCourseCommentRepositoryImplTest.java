package com.prgrms.p2p.domain.comment.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.prgrms.p2p.domain.comment.dto.CourseCommentForQueryDsl;
import com.prgrms.p2p.domain.comment.entity.CourseComment;
import com.prgrms.p2p.domain.comment.service.CourseCommentService;
import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.course.entity.Period;
import com.prgrms.p2p.domain.course.entity.Region;
import com.prgrms.p2p.domain.course.entity.Spot;
import com.prgrms.p2p.domain.course.entity.Theme;
import com.prgrms.p2p.domain.course.repository.CourseRepository;
import com.prgrms.p2p.domain.user.entity.Sex;
import com.prgrms.p2p.domain.user.entity.User;
import com.prgrms.p2p.domain.user.repository.UserRepository;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class SearchCourseCommentRepositoryImplTest {

  @Autowired
  UserRepository userRepository;

  @Autowired
  CourseRepository courseRepository;

  @Autowired
  CourseCommentRepository courseCommentRepository;

  @Autowired
  CourseCommentService courseCommentService;

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

    // 2번에 다는 대댓글
    courseCommentRepository.save(
        new CourseComment("    comment9", commentId2, userId, course));

    courseCommentRepository.save(
        new CourseComment("    comment10", commentId2, userId, course));
    courseCommentRepository.save(
        new CourseComment("    comment11", commentId2, userId, course));
    courseCommentRepository.save(
        new CourseComment("    comment12", commentId2, userId, course));

    // 1번에 다는 대댓글
    courseCommentRepository.save(
        new CourseComment("    comment4", commentId, userId, course));
    courseCommentRepository.save(
        new CourseComment("    comment5", commentId, userId, course));
    courseCommentRepository.save(
        new CourseComment("    comment6", commentId, userId, course));
    courseCommentRepository.save(
        new CourseComment("    comment7", commentId, userId, course));

    // 2번에 다는 대댓글
    courseCommentRepository.save(
        new CourseComment("    comment13", commentId2, userId, course));
    courseCommentRepository.save(
        new CourseComment("    comment14", commentId2, userId, course));
    courseCommentRepository.save(
        new CourseComment("    comment15", commentId2, userId, course));

    // then
    List<CourseCommentForQueryDsl> courseComments = courseCommentRepository.findCourseComment(course.getId());

    for (int i = 0; i < 15; i++) {
      assertThat(courseComments.get(i).getComment())
          .contains(String.valueOf(i + 1));
      System.out.println("comment = " + courseComments.get(i).getComment());
      System.out.println("comment = " + courseComments.get(i).getVisibility());
    }
  }

  @Test
  @DisplayName("코스에 댓글이 하나도 작성이 되지 않았다면 반환되는 값이 없습니다.")
  public void findCourseCommentIsEmpty() throws Exception {

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

    // 2번에 다는 대댓글
    courseCommentRepository.save(
        new CourseComment("    comment9", commentId2, userId, course));
    courseCommentRepository.save(
        new CourseComment("    comment10", commentId2, userId, course));
    courseCommentRepository.save(
        new CourseComment("    comment11", commentId2, userId, course));
    courseCommentRepository.save(
        new CourseComment("    comment12", commentId2, userId, course));

    // 1번에 다는 대댓글
    courseCommentRepository.save(
        new CourseComment("    comment4", commentId, userId, course));
    courseCommentRepository.save(
        new CourseComment("    comment5", commentId, userId, course));
    courseCommentRepository.save(
        new CourseComment("    comment6", commentId, userId, course));
    courseCommentRepository.save(
        new CourseComment("    comment7", commentId, userId, course));

    // 2번에 다는 대댓글
    courseCommentRepository.save(
        new CourseComment("    comment13", commentId2, userId, course));
    courseCommentRepository.save(
        new CourseComment("    comment14", commentId2, userId, course));
    courseCommentRepository.save(
        new CourseComment("    comment15", commentId2, userId, course));

    // then
    List<CourseCommentForQueryDsl> courseComments
        = courseCommentRepository.findCourseComment(-1L);

    assertThat(courseComments.size()).isEqualTo(0);
  }
}