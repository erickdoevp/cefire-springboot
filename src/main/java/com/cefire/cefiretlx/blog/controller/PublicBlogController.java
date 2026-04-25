package com.cefire.cefiretlx.blog.controller;

import com.cefire.cefiretlx.blog.domain.BlogStatus;
import com.cefire.cefiretlx.blog.dto.BlogDetailResponseDto;
import com.cefire.cefiretlx.blog.dto.BlogSummaryResponseDto;
import com.cefire.cefiretlx.blog.service.IBlogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/public/blogs")
@RequiredArgsConstructor
public class PublicBlogController {

  private final IBlogService blogService;

  @GetMapping
  ResponseEntity<Page<BlogSummaryResponseDto>> findAllPaginated(
      @RequestParam(required = false) String title,
      @RequestParam(required = false) Long categoryId,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime updatedFrom,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime updatedTo,
      @PageableDefault(page = 0, size = 10, sort = "updatedAt") Pageable pageable) {
    return ResponseEntity.ok(
        blogService.findAllPaginated(title, BlogStatus.PUBLISHED, categoryId, updatedFrom, updatedTo, pageable)
    );
  }

  @GetMapping("/{slug}")
  ResponseEntity<BlogDetailResponseDto> findBySlug(@PathVariable String slug) {
    return ResponseEntity.ok(blogService.findBySlug(slug));
  }

}
