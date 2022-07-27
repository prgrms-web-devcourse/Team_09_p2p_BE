package com.prgrms.p2p.domain.like.entity;

import com.prgrms.p2p.domain.common.BaseEntity;
import com.prgrms.p2p.domain.course.entity.Course;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;


@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")

@Entity
@Table(name = "comment")
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE room SET is_deleted = true WHERE id = ?")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Like extends BaseEntity {

  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  private Long userId;

  @Column(name = "is_deleted")
  private Boolean isDeleted = Boolean.FALSE;
}
