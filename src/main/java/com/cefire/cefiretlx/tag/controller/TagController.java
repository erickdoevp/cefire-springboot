package com.cefire.cefiretlx.tag.controller;

import com.cefire.cefiretlx.tag.dto.TagDetailResponseDto;
import com.cefire.cefiretlx.tag.dto.TagRequestDto;
import com.cefire.cefiretlx.tag.dto.TagResponseDto;
import com.cefire.cefiretlx.tag.service.ITagService;
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
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
@Tag(name = "Tags", description = "Operaciones relacionadas con la gestión de tags")
public class TagController {

  private final ITagService tagService;

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Listar tags", description = "Retorna la lista completa de tags disponibles")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
      @ApiResponse(responseCode = "403", description = "Acceso denegado")
  })
  ResponseEntity<List<TagResponseDto>> getAllTags() {
    return ResponseEntity.ok(tagService.findAll());
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Obtener tag por ID", description = "Retorna el detalle completo de un tag por su ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Tag encontrado exitosamente"),
      @ApiResponse(responseCode = "403", description = "Acceso denegado"),
      @ApiResponse(responseCode = "404", description = "Tag no encontrado")
  })
  ResponseEntity<TagDetailResponseDto> findTagById(@PathVariable Long id) {
    return ResponseEntity.ok(tagService.findById(id));
  }

  @PostMapping
  @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
  @Operation(summary = "Crear tag", description = "Crea un nuevo tag manualmente. Los tags también se crean automáticamente al guardar un blog")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Tag creado exitosamente"),
      @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
      @ApiResponse(responseCode = "403", description = "Acceso denegado"),
      @ApiResponse(responseCode = "409", description = "Ya existe un tag con ese nombre")
  })
  ResponseEntity<TagResponseDto> saveTag(@Valid @RequestBody TagRequestDto tagRequestDto) {
    return new ResponseEntity<>(tagService.save(tagRequestDto), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Actualizar tag", description = "Actualiza el nombre de un tag existente")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Tag actualizado exitosamente"),
      @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
      @ApiResponse(responseCode = "403", description = "Acceso denegado"),
      @ApiResponse(responseCode = "404", description = "Tag no encontrado")
  })
  ResponseEntity<TagResponseDto> updateTag(@PathVariable Long id, @Valid @RequestBody TagRequestDto tagRequestDto) {
    return ResponseEntity.ok(tagService.update(id, tagRequestDto));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Eliminar tag", description = "Elimina permanentemente un tag por su ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Tag eliminado exitosamente"),
      @ApiResponse(responseCode = "403", description = "Acceso denegado"),
      @ApiResponse(responseCode = "404", description = "Tag no encontrado")
  })
  ResponseEntity<Void> deleteTag(@PathVariable Long id) {
    tagService.deleteTag(id);
    return ResponseEntity.noContent().build();
  }

}
