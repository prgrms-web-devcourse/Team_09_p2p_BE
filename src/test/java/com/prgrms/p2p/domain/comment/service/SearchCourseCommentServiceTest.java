package com.prgrms.p2p.domain.comment.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.prgrms.p2p.domain.comment.dto.CourseCommentDto;
import com.prgrms.p2p.domain.comment.dto.CourseCommentResponse;
import com.prgrms.p2p.domain.comment.entity.CourseComment;
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
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class SearchCourseCommentServiceTest {

  @Autowired
  UserRepository userRepository;

  @Autowired
  CourseRepository courseRepository;

  @Autowired
  CourseCommentRepository courseCommentRepository;

  @Autowired
  CourseCommentService courseCommentService;

  @Autowired
  SearchCourseCommentService searchCourseCommentService;

  @BeforeEach
  void setup() {
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

    user = userRepository.save(new User(email, password, nick, birth, male));

    course = courseRepository.save(
        new Course(title, oneDay, region, themes, spots, user));

    // parent comment 1
    parentComment1 = courseCommentRepository.save(
        new CourseComment("parentComment1.                 작성시간 : " + LocalTime.now()
            , null, user.getId(), course));

    courseCommentRepository.save(
        new CourseComment("------> 1번의 첫번째 sub 댓글이에요. 작성시간 : " + LocalTime.now(),
            parentComment1.getId(), user.getId(), course));

    // parent comment 2
    parentComment2 = courseCommentRepository.save(
        new CourseComment("parentComment2.                 작성시간 : " + LocalTime.now(), null,
            user.getId(), course));

    middleSubComment = courseCommentRepository.save(
        new CourseComment("------> 1번의 두번째 sub 댓글이에요. 작성시간 : " + LocalTime.now(),
            parentComment1.getId(), user.getId(), course));
    courseCommentRepository.save(
        new CourseComment("------> 2번의 첫번째 sub 댓글이에요. 작성시간 : " + LocalTime.now(),
            parentComment2.getId(), user.getId(), course));

    courseCommentRepository.save(
        new CourseComment("------> 2번의 두번째 sub 댓글이에요. 작성시간 : " + LocalTime.now(),
            parentComment2.getId(), user.getId(), course));
    courseCommentRepository.save(
        new CourseComment("------> 2번의 세번째 sub 댓글이에요. 작성시간 : " + LocalTime.now(),
            parentComment2.getId(), user.getId(), course));
    courseCommentRepository.save(
        new CourseComment("------> 1번의 세번째 sub 댓글이에요. 작성시간 : " + LocalTime.now(),
            parentComment1.getId(), user.getId(), course));

    // parent comment 3
    parentComment3 = courseCommentRepository.save(
        new CourseComment("parentComment3.                 작성시간 : " + LocalTime.now(),
            null, user.getId(), course));

    // parent comment 4
    parentComment4 = courseCommentRepository.save(
        new CourseComment("parentComment4.                 작성시간 : " + LocalTime.now(),
            null, user.getId(), course));

    lastSubComment = courseCommentRepository.save(
        new CourseComment("------> 4번의 첫번째 sub 댓글이에요. 작성시간 : " + LocalTime.now(),
            parentComment4.getId(), user.getId(), course));
  }

  CourseComment parentComment1;
  CourseComment parentComment2;
  CourseComment parentComment3;
  CourseComment parentComment4;

  CourseComment middleSubComment;
  CourseComment lastSubComment;

  Course course;
  User user;


  @Test
  @DisplayName("순서 보장: 인근 댓글끼리 비교했을 시"
      + "1. 둘다 getRootCommentId = null(부모댓글) : 생성 순서 오름차순,"
      + "2. 부모댓글 뒤 서브 댓글이면 : 서브댓글의 getRootCommentId는 앞의 부모 댓글, 생성 순서 오름차순"
      + "3. 둘다 서브댓글이면 : 둘의 부모댓글은 같으며 생성 순서 오름차순")
  public void getCorrectSequence() throws Exception {

    // when
    CourseCommentResponse response
        = searchCourseCommentService.findCourseComment(course.getId());

    for (int i = 0; i < response.getCourseComments().size() - 1; i++) {

      if (Objects.isNull(response.getCourseComments().get(i).getRootCommentId())) {

        if (Objects.isNull(response.getCourseComments().get(i + 1).getRootCommentId())) {
          assertThat(response.getCourseComments().get(i).getCreatedAt())
              .isBefore(response.getCourseComments().get(i + 1).getCreatedAt());

        } else {
          assertThat(response.getCourseComments().get(i).getId())
              .isEqualTo(response.getCourseComments().get(i + 1).getRootCommentId());
          assertThat(response.getCourseComments().get(i).getCreatedAt())
              .isBefore(response.getCourseComments().get(i + 1).getCreatedAt());
        }

      } else {
        if (!Objects.isNull(response.getCourseComments().get(i + 1).getRootCommentId())) {
          assertThat(response.getCourseComments().get(i).getRootCommentId())
              .isEqualTo(response.getCourseComments().get(i + 1).getRootCommentId());
          assertThat(response.getCourseComments().get(i).getCreatedAt())
              .isBefore(response.getCourseComments().get(i + 1).getCreatedAt());
        }
      }
    }

    // 결과 확인용
    for (CourseCommentDto courseCommentResponse : response.getCourseComments()) {
      System.out.println(
          "" + courseCommentResponse.getComment() + " / 작성자 = " + courseCommentResponse.getUser()
              .getNickname());
    }
  }

  @Test
  @DisplayName("대댓글이 없는 댓글을 삭제하면 표기되지 않습니다.")
  public void deleteParentCommentThatDontHasSubComment() throws Exception {

    // given
    CourseCommentResponse responseBefore
        = searchCourseCommentService.findCourseComment(course.getId());
    List<CourseCommentDto> beforeList = responseBefore.getCourseComments().stream()
        .filter(bl -> bl.getId().equals(parentComment3.getId()))
        .collect(Collectors.toList());

    assertThat(beforeList.size()).isEqualTo(1);

    // when
    courseCommentService.deleteCourseComment(parentComment3.getId(), course.getId(), user.getId());

    // then
    CourseCommentResponse responseAfter
        = searchCourseCommentService.findCourseComment(course.getId());

    List<CourseCommentDto> afterList = responseAfter.getCourseComments().stream()
        .filter(al -> al.getId().equals(parentComment3.getId()))
        .collect(Collectors.toList());

    assertThat(afterList.size()).isEqualTo(0);

    // 결과 확인용
    for (CourseCommentDto courseCommentResponse : responseAfter.getCourseComments()) {
      System.out.println(
          "" + courseCommentResponse.getComment() + " / 작성자 = " + courseCommentResponse.getUser()
              .getNickname());
    }
  }

  @Test
  @DisplayName("대댓글이 있는 댓글을 삭제하면 내용, 유저 닉네임, 프로필사진, 생성 및 수정일자가 표기되지 않습니다.")
  public void deleteParentCommentThatHasSubComment() throws Exception {

    // given
    CourseCommentResponse responseBefore
        = searchCourseCommentService.findCourseComment(course.getId());
    List<CourseCommentDto> beforeList = responseBefore.getCourseComments().stream()
        .filter(bl -> bl.getId().equals(parentComment4.getId()))
        .collect(Collectors.toList());

    assertThat(beforeList.size()).isEqualTo(1);

    // when
    courseCommentService.deleteCourseComment(parentComment4.getId(), course.getId(), user.getId());

    // then
    CourseCommentResponse responseAfter
        = searchCourseCommentService.findCourseComment(course.getId());

    List<CourseCommentDto> afterList = responseAfter.getCourseComments().stream()
        .filter(al -> al.getId().equals(parentComment4.getId()))
        .collect(Collectors.toList());
    assertThat(afterList.size()).isEqualTo(1);
    assertThat(afterList.get(0).getComment()).isEqualTo("삭제된 댓글입니다.");
    assertThat(afterList.get(0).getUser().getNickname()).isNull();
    assertThat(afterList.get(0).getUser().getProfileImage()).isNull();
    assertThat(afterList.get(0).getCreatedAt()).isNull();
    assertThat(afterList.get(0).getUpdatedAt()).isNull();

    // 결과 확인용
    for (CourseCommentDto courseCommentResponse : responseAfter.getCourseComments()) {
      System.out.println(
          "" + courseCommentResponse.getComment() + " / 작성자 = " + courseCommentResponse.getUser()
              .getNickname());
    }
  }

  @Test
  @DisplayName("대댓글을 삭제했을 때 삭제된 부모의 댓글에 더이상 대댓글이 존재하지 않는다면(삭제한 대댓글이 마지막 대댓글이었다면) 부모 댓글도 더 이상 표시되지 않습니다.")
  public void deleteLastSubCommentForDeletedParentComment() throws Exception {

    // given
    CourseCommentResponse responseBefore
        = searchCourseCommentService.findCourseComment(course.getId());
    List<CourseCommentDto> beforeList = responseBefore.getCourseComments().stream()
        .filter(bl -> bl.getId().equals(parentComment4.getId()))
        .collect(Collectors.toList());

    assertThat(beforeList.size()).isEqualTo(1);

    // when
    courseCommentService.deleteCourseComment(parentComment4.getId(), course.getId(), user.getId());
    courseCommentService.deleteCourseComment(lastSubComment.getId(), course.getId(), user.getId());

    // then
    CourseCommentResponse responseAfter
        = searchCourseCommentService.findCourseComment(course.getId());

    List<CourseCommentDto> afterList = responseAfter.getCourseComments().stream()
        .filter(al -> al.getId().equals(parentComment4.getId()))
        .collect(Collectors.toList());
    assertThat(afterList.size()).isEqualTo(0);

    // 결과 확인용
    for (CourseCommentDto courseCommentResponse : responseAfter.getCourseComments()) {
      System.out.println(
          "" + courseCommentResponse.getComment() + " / 작성자 = " + courseCommentResponse.getUser()
              .getNickname());
    }
  }

  @Test
  @DisplayName("마지막 대댓글을 삭제했을 때 부모댓글이 삭제 상태가 아니라면 부모 댓글도 더 이상 표시되지 않습니다.")
  public void deleteLastSubCommentForVisibilityTrueComment() throws Exception {

    // given
    CourseCommentResponse responseBefore
        = searchCourseCommentService.findCourseComment(course.getId());
    List<CourseCommentDto> beforeList = responseBefore.getCourseComments().stream()
        .filter(bl -> bl.getId().equals(parentComment4.getId()))
        .collect(Collectors.toList());

    assertThat(beforeList.size()).isEqualTo(1);

    // when
    courseCommentService.deleteCourseComment(lastSubComment.getId(), course.getId(), user.getId());

    // then
    CourseCommentResponse responseAfter
        = searchCourseCommentService.findCourseComment(course.getId());

    List<CourseCommentDto> afterList = responseAfter.getCourseComments().stream()
        .filter(al -> al.getId().equals(parentComment4.getId()))
        .collect(Collectors.toList());
    assertThat(afterList.size()).isEqualTo(1);

    // 결과 확인용
    for (CourseCommentDto courseCommentResponse : responseAfter.getCourseComments()) {
      System.out.println(
          "" + courseCommentResponse.getComment() + " / 작성자 = " + courseCommentResponse.getUser()
              .getNickname());
    }
  }

  @Test
  @DisplayName("대댓글은 바로 삭제되며 다른 대댓글이 있을 시 나머지는 그대로 유지됍니다.")
  public void deleteSubComment() throws Exception {

    // given
    CourseCommentResponse responseBefore
        = searchCourseCommentService.findCourseComment(course.getId());
    List<CourseCommentDto> beforeList = responseBefore.getCourseComments().stream()
        .filter(bl -> bl.getId().equals(middleSubComment.getId()))
        .collect(Collectors.toList());

    assertThat(beforeList.size()).isEqualTo(1);

    // when
    courseCommentService.deleteCourseComment(middleSubComment.getId(), course.getId(),
        user.getId());

    // then
    CourseCommentResponse responseAfter
        = searchCourseCommentService.findCourseComment(course.getId());

    List<CourseCommentDto> afterList = responseAfter.getCourseComments().stream()
        .filter(al -> al.getId().equals(middleSubComment.getId()))
        .collect(Collectors.toList());

    assertThat(afterList.size()).isEqualTo(0);

    // 결과 확인용
    for (CourseCommentDto courseCommentResponse : responseAfter.getCourseComments()) {
      System.out.println(
          "" + courseCommentResponse.getComment() + " / 작성자 = " + courseCommentResponse.getUser()
              .getNickname());
    }
  }
}