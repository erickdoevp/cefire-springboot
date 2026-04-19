package com.cefire.cefiretlx.category.controller;

import com.cefire.cefiretlx.category.dto.CategoryDetailResponseDto;
import com.cefire.cefiretlx.category.dto.CategoryRequestDto;
import com.cefire.cefiretlx.category.dto.CategoryResponseDto;
import com.cefire.cefiretlx.category.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

  private final ICategoryService categoryService;

  @GetMapping
  ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
    List<CategoryResponseDto> categories = categoryService.findAll();
    return ResponseEntity.ok(categories);
  };

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  ResponseEntity<CategoryDetailResponseDto> findCategoryDetailDyId(@PathVariable Long id) {
    return ResponseEntity.ok(categoryService.findById(id));
  };

  @PostMapping
  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  ResponseEntity<CategoryResponseDto> saveCategory(@RequestBody CategoryRequestDto categoryRequestDto) {
    return new ResponseEntity<>(categoryService.save(categoryRequestDto), HttpStatus.CREATED);
  };

  @PutMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN','USER')")
  ResponseEntity<CategoryResponseDto> updateCategory(@PathVariable Long id, @RequestBody CategoryRequestDto categoryRequestDto) {
    return ResponseEntity.ok(categoryService.update(id,categoryRequestDto));
  };

}
