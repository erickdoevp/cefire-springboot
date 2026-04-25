package com.cefire.cefiretlx.blog.domain;

import com.cefire.cefiretlx.category.domain.Category;
import com.cefire.cefiretlx.tag.domain.Tag;
import com.cefire.cefiretlx.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "blogs")
@EntityListeners(AuditingEntityListener.class)
public class Blog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false, unique = true)
  private String slug;

  @Column(nullable = false)
  private String excerpt;

  @Column(columnDefinition = "json", nullable = false)
  private String content;

  @Column(nullable = false)
  private String conclusion;

  @Column(nullable = false)
  private String featuredImage;

  @Column(nullable = false)
  private String metaDescription;

  @Column(nullable = false)
  private Integer readingTime;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private BlogStatus status;

  @ManyToMany
  @JoinTable(
      name = "blog_tags",
      joinColumns = @JoinColumn(name = "blog_id"),
      inverseJoinColumns = @JoinColumn(name = "tag_id")
  )
  private List<Tag> tags;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author_id", nullable = false)
  private User author;

  @CreatedDate
  @Column(updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  @Column()
  private LocalDateTime updatedAt;

}
