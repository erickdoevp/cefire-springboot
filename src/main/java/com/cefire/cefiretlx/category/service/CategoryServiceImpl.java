package com.cefire.cefiretlx.category.service;

import com.cefire.cefiretlx.category.domain.Category;
import com.cefire.cefiretlx.category.dto.CategoryDetailResponseDto;
import com.cefire.cefiretlx.category.dto.CategoryRequestDto;
import com.cefire.cefiretlx.category.dto.CategoryResponseDto;
import com.cefire.cefiretlx.category.mapper.CategoryMapper;
import com.cefire.cefiretlx.category.repository.CategoryRepository;
import com.cefire.cefiretlx.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService {

  private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

  private final CategoryRepository categoryRepository;
  private final CategoryMapper categoryMapper;

  @Override
  @Transactional
  public CategoryResponseDto save(CategoryRequestDto categoryRequestDto) {
    logger.info("Creando categoría con nombre='{}'", categoryRequestDto.getName());
    Category categoryToSave = categoryMapper.toEntity(categoryRequestDto);
    Category categorySaved = categoryRepository.save(categoryToSave);
    logger.info("Categoría creada exitosamente con id={}", categorySaved.getId());
    return categoryMapper.toResponseDto(categorySaved);
  }

  @Override
  @Transactional(readOnly = true)
  public List<CategoryResponseDto> findAll() {
    logger.debug("Obteniendo todas las categorías");
    List<CategoryResponseDto> result = categoryRepository.findAll()
        .stream()
        .map(categoryMapper::toResponseDto).toList();
    logger.debug("findAll: {} categorías encontradas", result.size());
    return result;
  }

  @Override
  @Transactional(readOnly = true)
  public CategoryDetailResponseDto findById(Long id) {
    logger.debug("Buscando categoría por id={}", id);
    Category category = categoryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con el id " + id));
    return categoryMapper.toDetailResponseDto(category);
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
    logger.info("Actualizando categoría id={}", id);
    Category categoryToUpdate = categoryRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Categoría no encontrada con el id " + id));
    categoryToUpdate.setName(categoryRequestDto.getName());
    categoryToUpdate.setChipColor(categoryRequestDto.getChipColor());
    categoryToUpdate.setTextChipColor(categoryRequestDto.getTextChipColor());
    Category saved = categoryRepository.save(categoryToUpdate);
    logger.info("Categoría id={} actualizada exitosamente", saved.getId());
    return categoryMapper.toResponseDto(saved);
  }

  @Override
  @Transactional
  public void deleteCategory(Long id) {
    logger.info("Eliminando categoría id={}", id);
    if (!categoryRepository.existsById(id)) {
      logger.warn("Intento de eliminar categoría inexistente con id={}", id);
      throw new ResourceNotFoundException("Categoría no encontrada con el id " + id);
    }
    categoryRepository.deleteById(id);
    logger.info("Categoría id={} eliminada exitosamente", id);
  }

}
