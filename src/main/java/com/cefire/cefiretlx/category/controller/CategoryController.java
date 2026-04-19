package com.cefire.cefiretlx.category.controller;

import com.cefire.cefiretlx.category.dto.CategoryResponseDto;
import com.cefire.cefiretlx.category.mapper.CategoryMapper;
import com.cefire.cefiretlx.category.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

  private final ICategoryService categoryService;
  private final CategoryMapper categoryMapper;

  @GetMapping
  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
    List<CategoryResponseDto> categories = categoryService.findAll();

    return ResponseEntity.ok(categories);

  };

}
