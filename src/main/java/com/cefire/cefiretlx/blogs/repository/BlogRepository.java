package com.cefire.cefiretlx.blogs.repository;

import com.cefire.cefiretlx.blogs.domain.Blog;
import com.cefire.cefiretlx.blogs.domain.BlogStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {

  Optional<Blog> findBySlug(String slug);

  boolean existsBySlug(String slug);

  List<Blog> findByStatus(BlogStatus status);

  List<Blog> findByCategoryId(Long categoryId);

  List<Blog> findByAuthorId(UUID authorId);

}
