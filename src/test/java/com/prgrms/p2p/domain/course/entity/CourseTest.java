package com.prgrms.p2p.domain.course.entity;

import com.prgrms.p2p.domain.user.entity.Sex;
import com.prgrms.p2p.domain.user.entity.User;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;

class CourseTest {

  Course course;
  String title, description;
  Period period;
  Region region;
  Set<Theme> themes;
  Set<Spot> spots;
  User user;

  @BeforeEach
  void setup() {
    title = "title";
    description = "description";
    period = Period.ONE_DAY;
    region = Region.서울;
    themes = Set.of(Theme.가족여행);
    spots = Set.of(Spot.바다);
    user = new User("dhkstnaos@gmail.com", "1234", "asdf", "1997-11-29", Sex.FEMALE);
    course = new Course(title, period, region, description, themes, spots, user);
  }
}