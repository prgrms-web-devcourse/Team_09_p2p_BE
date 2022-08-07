package com.prgrms.p2p.domain.comment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.prgrms.p2p.domain.comment.dto.CreateCommentRequest;
import com.prgrms.p2p.domain.comment.dto.UpdateCommentRequest;
import com.prgrms.p2p.domain.comment.entity.CourseComment;
import com.prgrms.p2p.domain.comment.entity.Visibility;
import com.prgrms.p2p.domain.comment.repository.CourseCommentRepository;
import com.prgrms.p2p.domain.course.entity.Course;
import com.prgrms.p2p.domain.course.entity.Period;
import com.prgrms.p2p.domain.course.entity.Region;
import com.prgrms.p2p.domain.course.entity.Spot;
import com.prgrms.p2p.domain.course.entity.Theme;
import com.prgrms.p2p.domain.course.repository.CourseRepository;
import com.prgrms.p2p.domain.user.entity.Sex;
import com.prgrms.p2p.domain.user.entity.User;
import com.prgrms.p2p.domain.user.repository.UserRepository;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class CourseCommentServiceTest {

  @Autowired
  CourseCommentService courseCommentService;

  @Autowired
  CourseCommentRepository courseCommentRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  CourseRepository courseRepository;

  @BeforeEach
  void setup() {
    //course
    String title = "title";
    Period oneDay = Period.ONE_DAY;
    Region region = Region.경기;
    String description = "description";
    Set<Theme> themes = new HashSet<>();
    Set<Spot> spots = new HashSet<>();
    String email = "e@mail.com";
    String password = "password";
    String nick = "nick";
    String birth = "2010-01-01";
    Sex male = Sex.MALE;

    user = userRepository.save(new User(email, password, nick, birth, male));
    Long userId = user.getId();

    course = courseRepository.save(
        new Course(title, oneDay, region, description, themes, spots, user));

    //comment
    rootCommentId1 = courseCommentRepository.save(
        new CourseComment("이것은 댓글1 입니다.", null, userId, course)).getId();

    rootCommentId2 = courseCommentRepository.save(
        new CourseComment("이것은 댓글2 입니다.", null, userId, course)).getId();

    lastSubComment = courseCommentRepository.save(
        new CourseComment("------> 4번의 첫번째 sub 댓글이에요. 작성시간 : " + LocalTime.now(),
            rootCommentId2, user.getId(), course));
  }

  Long rootCommentId1;
  Long rootCommentId2;
  CourseComment lastSubComment;
  Course course;
  User user;


  @Nested
  @DisplayName("코스 댓글 생성")
  class createCourseComment {

    @Test
    @DisplayName("성공: 코스에 댓글을 작성합니다. -> rootCommentId = null")
    public void createCourseRootComment() throws Exception {

      //given
      String comment = "이것은 댓글입니다.";
      Long rootCommentId = null;
      Long userId = user.getId();
      Long courseId = course.getId();

      CreateCommentRequest createCommentReq = CreateCommentRequest.builder()
          .comment(comment)
          .rootCommentId(rootCommentId)
          .build();

      //when
      Long courseCommentId = courseCommentService.save(createCommentReq, courseId, userId);
      CourseComment courseComment = courseCommentRepository.findById(courseCommentId)
          .orElseThrow(RuntimeException::new);

      //then
      assertThat(courseComment.getComment()).isEqualTo(comment);
      assertThat(courseComment.getCourse()).isEqualTo(course);
      assertThat(courseComment.getRootCommentId()).isNull();
      assertThat(courseComment.getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("실패: userId에 해당하는 유저가 존재하지 않는다면 댓글을 작성할 수 없습니다.")
    public void failCreateCommentAsNotExistUser() throws Exception {

      //given
      String comment = "이것은 실패할 댓글입니다.";
      Long rootCommentId = null;
      Long userId = -1L;
      Long courseId = course.getId();

      CreateCommentRequest createCommentReq = CreateCommentRequest.builder()
          .comment(comment)
          .rootCommentId(rootCommentId)
          .build();

      //then
      assertThrows(RuntimeException.class,
          () -> courseCommentService.save(createCommentReq, courseId, userId));
    }

    @Test
    @DisplayName("실패: courseId에 해당하는 코스 게시물이 존재하지 않는다면 댓글을 작성할 수 없습니다.")
    public void failCreateCommentAsNotExistCourse() throws Exception {

      //given
      String comment = "이것은 실패할 댓글입니다.";
      Long rootCommentId = null;
      Long userId = user.getId();
      Long courseId = -1L;

      CreateCommentRequest createCommentReq = CreateCommentRequest.builder()
          .comment(comment)
          .rootCommentId(rootCommentId)
          .build();

      //then
      assertThrows(RuntimeException.class,
          () -> courseCommentService.save(createCommentReq, courseId, userId));
    }

    @Test
    @DisplayName("성공: 코스에 대댓글을 작성합니다. -> rootCommentId = notNull")
    public void createCourseSubComment() throws Exception {

      //given
      String comment = "이것은 대댓글1 입니다.";
      Long rootCommentId = rootCommentId1;
      Long userId = user.getId();
      Long courseId = course.getId();
      CreateCommentRequest createCommentReq = CreateCommentRequest.builder()
          .comment(comment)
          .rootCommentId(rootCommentId)
          .build();

      //when
      Long courseCommentId = courseCommentService.save(createCommentReq, courseId, userId);
      CourseComment courseComment = courseCommentRepository.findById(courseCommentId)
          .orElseThrow(RuntimeException::new);

      //then
      assertThat(courseComment.getComment()).isEqualTo(comment);
      assertThat(courseComment.getCourse()).isEqualTo(course);
      assertThat(courseComment.getRootCommentId()).isEqualTo(rootCommentId1);
      assertThat(courseComment.getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("실패: root 댓글이 없을 경우 하위 대댓글을 작성할 수 없습니다.")
    public void failCreateSubCommentAsNotExistCourse() throws Exception {

      //given
      String comment = "이것은 실패할 대댓글입니다.";
      Long rootCommentId = -1L;
      Long userId = user.getId();
      Long courseId = course.getId();

      CreateCommentRequest createCommentReq = CreateCommentRequest.builder()
          .comment(comment)
          .rootCommentId(rootCommentId)
          .build();

      //then
      assertThrows(RuntimeException.class,
          () -> courseCommentService.save(createCommentReq, courseId, userId));
    }
  }

  @Nested
  @DisplayName("댓글 수정 기능 테스트")
  class update {

    @Test
    @DisplayName("성공: 댓글을 정상적으로 수정합니다.")
    public void updateComment() throws Exception {

      //given
      String newComment = "new Comment";
      UpdateCommentRequest updateReq = UpdateCommentRequest.builder()
          .comment(newComment)
          .build();

      //when
      Long changedCommentId
          = courseCommentService.updateCourseComment(
          updateReq, rootCommentId1, course.getId(), user.getId());

      //then
      CourseComment courseComment = courseCommentRepository.findById(changedCommentId)
          .orElseThrow(RuntimeException::new);

      assertThat(courseComment.getComment()).isEqualTo(newComment);
    }

    @Test
    @DisplayName("실패: 유저가 수정 권한이 없습니다.")
    public void failUpdateCommentAsWrongUser() throws Exception {

      //given
      String newComment = "new Comment";
      UpdateCommentRequest updateReq = UpdateCommentRequest.builder()
          .comment(newComment)
          .build();

      //when
      Long userId = -1L;

      //then
      assertThrows(RuntimeException.class,
          () -> courseCommentService.updateCourseComment(
              updateReq, rootCommentId1, course.getId(), userId));
    }

    @Test
    @DisplayName("실패: 댓글이 달려있는 게시글이 존재하지 않습니다.")
    public void failUpdateCommentAsWrongCourse() throws Exception {

      //given
      String newComment = "new Comment";
      UpdateCommentRequest updateReq = UpdateCommentRequest.builder()
          .comment(newComment)
          .build();

      //when
      Long courseId = -1L;

      //then
      assertThrows(RuntimeException.class,
          () -> courseCommentService.updateCourseComment(
              updateReq, rootCommentId1, courseId, user.getId()));
    }

    @Test
    @DisplayName("실패: 수정하려는 댓글이 존재하지 않습니다.")
    public void failUpdateCommentAsWrongComment() throws Exception {

      //given
      String newComment = "new Comment";
      UpdateCommentRequest updateReq = UpdateCommentRequest.builder()
          .comment(newComment)
          .build();

      //when
      Long commentId = -1L;

      //then
      assertThrows(RuntimeException.class, () -> courseCommentService.updateCourseComment(
          updateReq, commentId, course.getId(), user.getId()));
    }
  }

  @Nested
  @DisplayName("댓글 삭제 테스트")
  class deleteComment {

    @Test
    @DisplayName("대댓글이 없는 댓글을 삭제하면 visibility = false 입니다.")
    public void deleteParentCommentThatDontHasSubComment() throws Exception {

      //when
      courseCommentService.deleteCourseComment(rootCommentId1, course.getId(), user.getId());

      CourseComment courseComment = courseCommentRepository.findById(rootCommentId1)
          .orElseThrow(RuntimeException::new);

      //then
      assertThat(courseComment.getVisibility()).isEqualTo(Visibility.FALSE);
    }

    @Test
    @DisplayName("대댓글을 삭제하면 visibility = false 입니다.")
    public void deleteSubComment() throws Exception {

      //when
      courseCommentService.deleteCourseComment(lastSubComment.getId(), course.getId(), user.getId());

      CourseComment courseComment = courseCommentRepository.findById(lastSubComment.getId())
          .orElseThrow(RuntimeException::new);

      //then
      assertThat(courseComment.getVisibility()).isEqualTo(Visibility.FALSE);
    }

    @Test
    @DisplayName("대댓글이 있는 댓글을 삭제하면 visibility = deleted_information 입니다.")
    public void deleteParentCommentThatHasSubComment() throws Exception {

      //when
      courseCommentService.deleteCourseComment(rootCommentId2, course.getId(), user.getId());

      CourseComment courseComment = courseCommentRepository.findById(rootCommentId2)
          .orElseThrow(RuntimeException::new);

      //then
      assertThat(courseComment.getVisibility()).isEqualTo(Visibility.DELETED_INFORMATION);
    }

    @Test
    @DisplayName("삭제된 댓글의 마지막 대댓글을 삭제하면(대댓글이 더이상 존재하지 않는다면), 댓글의 visibility = false 입니다")
    public void commentVisibilityIsFalseWhenDeleteLastSubComment() throws Exception {

      //when
      courseCommentService.deleteCourseComment(rootCommentId2, course.getId(), user.getId());
      courseCommentService.deleteCourseComment(lastSubComment.getId(), course.getId(), user.getId());


      CourseComment courseComment = courseCommentRepository.findById(rootCommentId2)
          .orElseThrow(RuntimeException::new);

      //then
      assertThat(courseComment.getVisibility()).isEqualTo(Visibility.FALSE);
    }

    @Test
    @DisplayName("실패: 댓글 삭제 권한이 없다면(댓글의 userId와 입력 userId가 다르다면) 예외 발생")
    public void failAsWrongUserId() throws Exception {

      //then
      assertThrows(RuntimeException.class,
          () -> courseCommentService.deleteCourseComment(rootCommentId1, course.getId(), -1L));
    }

    @Test
    @DisplayName("실패: 입력 courseId를 가진 course 가 존재하지 않는다면 예외 발생")
    public void failAsWrongCourseId() throws Exception {

      //then
      assertThrows(RuntimeException.class,
          () -> courseCommentService.deleteCourseComment(rootCommentId1, -1L, user.getId()));
    }

    @Test
    @DisplayName("실패: 입력 commentId를 가진 comment 가 존재하지 않는다면 예외 발생")
    public void failAsWrongCommentId() throws Exception {

      //then
      assertThrows(RuntimeException.class,
          () -> courseCommentService.deleteCourseComment(-1L, course.getId(), user.getId()));
    }
  }
}