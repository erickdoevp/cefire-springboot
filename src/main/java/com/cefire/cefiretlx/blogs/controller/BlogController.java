package com.cefire.cefiretlx.blogs.controller;

import com.cefire.cefiretlx.blogs.domain.BlogStatus;
import com.cefire.cefiretlx.blogs.dto.BlogCreateRequestDto;
import com.cefire.cefiretlx.blogs.dto.BlogDetailResponseDto;
import com.cefire.cefiretlx.blogs.dto.BlogSummaryResponseDto;
import com.cefire.cefiretlx.blogs.dto.BlogUpdateRequestDto;
import com.cefire.cefiretlx.blogs.service.IBlogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/blogs")
@RequiredArgsConstructor
public class BlogController {

  private final IBlogService blogService;

  @GetMapping
  ResponseEntity<Page<BlogSummaryResponseDto>> findAllPaginated(
      @RequestParam(required = false) String title,
      @RequestParam(required = false) BlogStatus status,
      @RequestParam(required = false) Long categoryId,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime updatedFrom,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime updatedTo,
      @PageableDefault(page = 0, size = 10, sort = "updatedAt") Pageable pageable) {
    return ResponseEntity.ok(blogService.findAllPaginated(title, status, categoryId, updatedFrom, updatedTo, pageable));
  }

  @GetMapping("/all")
  ResponseEntity<List<BlogDetailResponseDto>> findAll() {
    return ResponseEntity.ok(blogService.findAll());
  }

  @GetMapping("/{id}")
  ResponseEntity<BlogDetailResponseDto> findById(@PathVariable Long id) {
    return ResponseEntity.ok(blogService.findById(id));
  }

  @GetMapping("/slug/{slug}")
  ResponseEntity<BlogDetailResponseDto> findBySlug(@PathVariable String slug) {
    return ResponseEntity.ok(blogService.findBySlug(slug));
  }

  @GetMapping("/status/{status}")
  ResponseEntity<List<BlogDetailResponseDto>> findByStatus(@PathVariable BlogStatus status) {
    return ResponseEntity.ok(blogService.findByStatus(status));
  }

  @GetMapping("/category/{categoryId}")
  ResponseEntity<List<BlogDetailResponseDto>> findByCategoryId(@PathVariable Long categoryId) {
    return ResponseEntity.ok(blogService.findByCategoryId(categoryId));
  }

  @GetMapping("/author/{authorId}")
  ResponseEntity<List<BlogDetailResponseDto>> findByAuthorId(@PathVariable UUID authorId) {
    return ResponseEntity.ok(blogService.findByAuthorId(authorId));
  }

  @PostMapping
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  ResponseEntity<BlogDetailResponseDto> create(@Valid @RequestBody BlogCreateRequestDto dto) {
    return new ResponseEntity<>(blogService.create(dto), HttpStatus.CREATED);
  }

  @PatchMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  ResponseEntity<BlogDetailResponseDto> update(@PathVariable Long id, @Valid @RequestBody BlogUpdateRequestDto dto) {
    return ResponseEntity.ok(blogService.update(id, dto));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  ResponseEntity<Void> delete(@PathVariable Long id) {
    blogService.delete(id);
    return ResponseEntity.noContent().build();
  }

}
