package com.cefire.cefiretlx.category.service;

import com.cefire.cefiretlx.category.domain.Category;
import com.cefire.cefiretlx.category.dto.CategoryRequestDto;
import com.cefire.cefiretlx.category.dto.CategoryResponseDto;
import com.cefire.cefiretlx.category.mapper.CategoryMapper;
import com.cefire.cefiretlx.category.repository.CategoryRepository;
import com.cefire.cefiretlx.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService {

  private final CategoryRepository categoryRepository;
  private final CategoryMapper categoryMapper;

  @Override
  @Transactional
  public CategoryResponseDto save(CategoryRequestDto categoryRequestDto) {
    Category categoryToSave = categoryMapper.toEntity(categoryRequestDto);
    Category categorySaved = categoryRepository.save(categoryToSave);
    return categoryMapper.toResponseDto(categorySaved);
  }

  @Override
  @Transactional(readOnly = true)
  public List<CategoryResponseDto> findAll() {
    return categoryRepository.findAll()
        .stream()
        .map(categoryMapper::toResponseDto).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public CategoryResponseDto findById(Long id) {
    Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con el id " + id));

    return categoryMapper.toResponseDto(category);
  }

  @Override
  @Transactional(readOnly = true)
  public Category findEntityById(Long id) {
    return categoryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con el id " + id));
  }

  @Override
  @Transactional
  public CategoryResponseDto update(Long id, CategoryRequestDto categoryRequestDto) {
    Category categoryToUpdate = categoryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con el id " + id));
    categoryToUpdate.setName(categoryRequestDto.getName());
    categoryToUpdate.setChipColor(categoryRequestDto.getChipColor());
    categoryToUpdate.setTextChipColor(categoryRequestDto.getTextChipColor());


    return categoryMapper.toResponseDto(categoryRepository.save(categoryToUpdate));
  }

  @Override
  @Transactional
  public void deleteCategory(Long id) {
    if(!categoryRepository.existsById(id)) {
      throw new ResourceNotFoundException("Categoría no encontrada con el id " + id);
    };
    categoryRepository.deleteById(id);
  }

}
