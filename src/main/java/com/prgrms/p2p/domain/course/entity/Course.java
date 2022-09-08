package com.prgrms.p2p.domain.course.entity;

import com.prgrms.p2p.domain.bookmark.entity.CourseBookmark;
import com.prgrms.p2p.domain.comment.entity.CourseComment;
import com.prgrms.p2p.domain.common.BaseEntity;
import com.prgrms.p2p.domain.common.exception.BadRequestException;
import com.prgrms.p2p.domain.like.entity.CourseLike;
import com.prgrms.p2p.domain.user.entity.User;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "course")
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE course SET is_deleted = true WHERE id = ?")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course extends BaseEntity {

  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  @Column(name = "title")
  private String title;

  @Enumerated(EnumType.STRING)
  @Column(name = "period")
  private Period period;

  @Enumerated(EnumType.STRING)
  @Column(name = "region")
  private Region region;

  @Enumerated(EnumType.STRING)
  @ElementCollection(fetch = FetchType.EAGER)
  @Column(name = "themes")
  private Set<Theme> themes = new HashSet<>();

  @Enumerated(EnumType.STRING)
  @ElementCollection(fetch = FetchType.EAGER)
  @Column(name = "spots")
  private Set<Spot> spots = new HashSet<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CoursePlace> coursePlaces = new ArrayList<>();

  @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CourseComment> courseComments = new ArrayList<>();

  @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CourseBookmark> courseBookmarks = new ArrayList<>();

  @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CourseLike> courseLikes = new ArrayList<>();

  @Column(name = "is_deleted")
  private Boolean isDeleted = Boolean.FALSE;

  public Course(String title, Period period, Region region, Set<Theme> themes, Set<Spot> spots,
      User user) {
    setTitle(title);
    setPeriod(period);
    setRegion(region);
    setThemes(themes);
    setSpots(spots);
    addUser(user);
  }

  public void changeTitle(String newTitle) {
    if (Strings.isBlank(newTitle)) {
      throw new BadRequestException("제목에 빈 값을 받을 수 없습니다.");
    }
    this.title = newTitle;
  }

  public void changePeriod(Period newPeriod) {
    if (Objects.isNull(newPeriod)) {
      throw new BadRequestException("기간에 빈 값을 받을 수 없습니다.");
    }
    this.period = newPeriod;
  }

  public void changeRegion(Region newRegion) {
    if (Objects.isNull(newRegion)) {
      throw new BadRequestException("지역에 빈 값을 받을 수 없습니다.");
    }
    this.region = newRegion;
  }

  public void changeThemes(List<Theme> themes) {
    this.themes = Set.copyOf(themes);
  }

  public void changeSpots(List<Spot> spots) {
    this.spots = Set.copyOf(spots);
  }

  public void addUser(User user) {
    if (Objects.isNull(user)) {
      throw new BadRequestException("유저에 빈 값을 받을 수 없습니다.");
    }
    if (this.user != null) {
      this.user.getCourses().remove(this);
    }
    user.addCourse(this);
    this.user = user;
  }

  public void addTheme(Theme theme) {
    this.themes.add(theme);
  }

  public void deleteTheme(Theme theme) {
    this.themes.remove(theme);
  }

  public void addCoursePlace(CoursePlace coursePlace) {
    this.coursePlaces.add(coursePlace);
  }

  public void deleteCoursePlace(CoursePlace coursePlace) {
    this.coursePlaces.remove(coursePlace);
  }

  public void addCourseComment(CourseComment courseComment) {
    this.courseComments.add(courseComment);
  }

  public void addCourseBookmark(CourseBookmark courseBookmark) {
    this.courseBookmarks.add(courseBookmark);
  }

  public void deleteCourseBookmark(CourseBookmark courseBookmark) {
    this.courseBookmarks.remove(courseBookmark);
  }

  public void addCourseLike(CourseLike courseLike) {
    this.courseLikes.add(courseLike);
  }

  public void deleteCourseLike(CourseLike courseLike) {
    this.courseLikes.remove(courseLike);
  }

  public boolean getAuthForDelete(User user) {

    if (user.getAuthorities().contains("ROLE_ADMIN")) {
      return true;
    }

    if (this.user.equals(user)) {
      return true;
    }
    return false;
  }

  private void setTitle(String title) {
    if (Strings.isBlank(title)) {
      throw new BadRequestException("제목에 값을 받을 수 없습니다.");
    }
    this.title = title;
  }

  private void setPeriod(Period period) {
    if (Objects.isNull(period)) {
      throw new BadRequestException("기간에 값을 받을 수 없습니다.");
    }
    this.period = period;
  }

  private void setRegion(Region region) {
    if (Objects.isNull(region)) {
      throw new BadRequestException("지역에 값을 받을 수 없습니다.");
    }
    this.region = region;
  }

  private void setThemes(Set<Theme> themes) {
    this.themes = themes;
  }

  private void setSpots(Set<Spot> spots) {
    this.spots = spots;
  }

  private void setUser(User user) {
    if (Objects.isNull(user)) {
      throw new IllegalArgumentException();
    }
    this.user = user;
  }

  private void setCoursePlaces(List<CoursePlace> coursePlaces) {
    if (coursePlaces.isEmpty()) {
      throw new IllegalArgumentException();
    }
    this.coursePlaces = coursePlaces;
  }

  private void setCourseComments(List<CourseComment> courseComments) {
    this.courseComments = courseComments;
  }

  private void setCourseBookmarks(List<CourseBookmark> courseBookmarks) {
    this.courseBookmarks = courseBookmarks;
  }

  private void setCourseLikes(List<CourseLike> courseLikes) {
    this.courseLikes = courseLikes;
  }
}
