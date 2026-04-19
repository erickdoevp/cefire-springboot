package com.cefire.cefiretlx.category.mapper;

import com.cefire.cefiretlx.category.domain.Category;
import com.cefire.cefiretlx.category.dto.CategoryRequestDto;
import com.cefire.cefiretlx.category.dto.CategoryResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

  @Mapping(target = "id", ignore = true)
  Category toEntity(CategoryRequestDto categoryRequestDto);

  CategoryResponseDto toResponseDto(Category category);

  // Método para actualizar una entidad existente
  @Mapping(target = "id", ignore = true)
  void updateCategoryFromDto(CategoryRequestDto dto, @MappingTarget Category category);

}
