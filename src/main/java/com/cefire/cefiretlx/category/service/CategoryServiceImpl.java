package com.cefire.cefiretlx.category.service;

import com.cefire.cefiretlx.category.domain.Category;
import com.cefire.cefiretlx.category.dto.CategoryRequestDto;
import com.cefire.cefiretlx.category.dto.CategoryResponseDto;
import com.cefire.cefiretlx.category.mapper.CategoryMapper;
import com.cefire.cefiretlx.category.repository.CategoryRepository;
import com.cefire.cefiretlx.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService {

  private final CategoryRepository categoryRepository;
  private final CategoryMapper categoryMapper;

  @Override
  public CategoryResponseDto save(CategoryRequestDto categoryRequestDto) {
    Category categoryToSave = categoryMapper.toEntity(categoryRequestDto);
    Category categorySaved = categoryRepository.save(categoryToSave);
    return categoryMapper.toResponseDto(categorySaved);
  }

  @Override
  public List<CategoryResponseDto> findAll() {
    return categoryRepository.findAll()
        .stream()
        .map(categoryMapper::toResponseDto).toList();
  }

  @Override
  public CategoryResponseDto findById(Long id) {
    Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con el id " + id));

    return categoryMapper.toResponseDto(category);
  }

  @Override
  public Category findEntityById(Long id) {
    return categoryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con el id " + id));
  }

  @Override
  public CategoryResponseDto update(Long id, CategoryRequestDto categoryRequestDto) {
    Category categoryToUpdate = categoryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con el id " + id));
    categoryToUpdate.setName(categoryRequestDto.getName());
    categoryToUpdate.setChipColor(categoryRequestDto.getChipColor());
    categoryToUpdate.setTextChipColor(categoryRequestDto.getTextChipColor());


    return categoryMapper.toResponseDto(categoryRepository.save(categoryToUpdate));
  }

  @Override
  public void deleteCategory(Long id) {
    if(!categoryRepository.existsById(id)) {
      throw new ResourceNotFoundException("Categoría no encontrada con el id " + id);
    };
    categoryRepository.deleteById(id);
  }
}
