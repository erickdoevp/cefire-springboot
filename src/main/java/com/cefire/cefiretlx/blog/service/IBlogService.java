package com.cefire.cefiretlx.blog.service;

import com.cefire.cefiretlx.blog.domain.Blog;
import com.cefire.cefiretlx.blog.domain.BlogStatus;
import com.cefire.cefiretlx.blog.dto.BlogCreateRequestDto;
import com.cefire.cefiretlx.blog.dto.BlogDetailResponseDto;
import com.cefire.cefiretlx.blog.dto.BlogSummaryResponseDto;
import com.cefire.cefiretlx.blog.dto.BlogUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface IBlogService {

  Page<BlogSummaryResponseDto> findAllPaginated(String title, BlogStatus status, Long categoryId, LocalDateTime updatedFrom, LocalDateTime updatedTo, Pageable pageable);

  BlogDetailResponseDto create(BlogCreateRequestDto dto);

  BlogDetailResponseDto findById(Long id);

  BlogDetailResponseDto findBySlug(String slug);

  Blog findEntityById(Long id);

  List<BlogDetailResponseDto> findAll();

  List<BlogDetailResponseDto> findByStatus(BlogStatus status);

  List<BlogDetailResponseDto> findByCategoryId(Long categoryId);

  List<BlogDetailResponseDto> findByAuthorId(UUID authorId);

  BlogDetailResponseDto update(Long id, BlogUpdateRequestDto dto);

  void delete(Long id);

}
