package com.cefire.cefiretlx.category.controller;

import com.cefire.cefiretlx.category.dto.CategoryDetailResponseDto;
import com.cefire.cefiretlx.category.dto.CategoryRequestDto;
import com.cefire.cefiretlx.category.dto.CategoryResponseDto;
import com.cefire.cefiretlx.category.service.ICategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Tag(name = "Categorías", description = "Operaciones relacionadas con la gestión de categorías")
public class CategoryController {

  private final ICategoryService categoryService;

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Listar categorías", description = "Retorna la lista completa de categorías disponibles")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
      @ApiResponse(responseCode = "403", description = "Acceso denegado")
  })
  ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
    List<CategoryResponseDto> categories = categoryService.findAll();
    return ResponseEntity.ok(categories);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Obtener categoría por ID", description = "Retorna el detalle completo de una categoría por su ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Categoría encontrada exitosamente"),
      @ApiResponse(responseCode = "403", description = "Acceso denegado"),
      @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
  })
  ResponseEntity<CategoryDetailResponseDto> findCategoryDetailDyId(@PathVariable Long id) {
    return ResponseEntity.ok(categoryService.findById(id));
  }

  @PostMapping
  @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
  @Operation(summary = "Crear categoría", description = "Crea una nueva categoría con nombre y colores de chip")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente"),
      @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
      @ApiResponse(responseCode = "403", description = "Acceso denegado"),
      @ApiResponse(responseCode = "409", description = "Ya existe una categoría con ese nombre")
  })
  ResponseEntity<CategoryResponseDto> saveCategory(@Valid @RequestBody CategoryRequestDto categoryRequestDto) {
    return new ResponseEntity<>(categoryService.save(categoryRequestDto), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Actualizar categoría", description = "Actualiza el nombre y colores de chip de una categoría existente")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Categoría actualizada exitosamente"),
      @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
      @ApiResponse(responseCode = "403", description = "Acceso denegado"),
      @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
  })
  ResponseEntity<CategoryResponseDto> updateCategory(@Valid @PathVariable Long id, @RequestBody CategoryRequestDto categoryRequestDto) {
    return ResponseEntity.ok(categoryService.update(id, categoryRequestDto));
  }

}
