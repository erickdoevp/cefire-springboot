package com.cefire.cefiretlx.blog.specification;

import com.cefire.cefiretlx.blog.domain.Blog;
import com.cefire.cefiretlx.blog.domain.BlogStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BlogSpecification {

  private BlogSpecification() {}

  public static Specification<Blog> withFilters(
      String title,
      BlogStatus status,
      Long categoryId,
      UUID authorId,
      LocalDateTime updatedFrom,
      LocalDateTime updatedTo
  ) {
    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (title != null && !title.isBlank()) {
        predicates.add(cb.like(cb.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
      }

      if (status != null) {
        predicates.add(cb.equal(root.get("status"), status));
      }

      if (categoryId != null) {
        predicates.add(cb.equal(root.get("category").get("id"), categoryId));
      }

      if (authorId != null) {
        predicates.add(cb.equal(root.get("author").get("id"), authorId));
      }

      if (updatedFrom != null) {
        predicates.add(cb.greaterThanOrEqualTo(root.get("updatedAt"), updatedFrom));
      }

      if (updatedTo != null) {
        predicates.add(cb.lessThanOrEqualTo(root.get("updatedAt"), updatedTo));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }

}
