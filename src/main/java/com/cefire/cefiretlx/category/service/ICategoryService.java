package com.cefire.cefiretlx.category.service;

import com.cefire.cefiretlx.category.domain.Category;
import com.cefire.cefiretlx.category.dto.CategoryDetailResponseDto;
import com.cefire.cefiretlx.category.dto.CategoryRequestDto;
import com.cefire.cefiretlx.category.dto.CategoryResponseDto;

import java.util.List;

public interface ICategoryService {
  CategoryResponseDto save(CategoryRequestDto categoryRequestDto);
  List<CategoryResponseDto> findAll();
  CategoryDetailResponseDto findById(Long id);
  Category findEntityById(Long id);
  CategoryResponseDto update(Long id, CategoryRequestDto categoryRequestDto);
  void deleteCategory(Long id);
}
