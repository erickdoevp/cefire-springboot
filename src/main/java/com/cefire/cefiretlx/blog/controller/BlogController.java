package com.cefire.cefiretlx.blog.controller;

import com.cefire.cefiretlx.blog.domain.BlogStatus;
import com.cefire.cefiretlx.blog.dto.BlogCreateRequestDto;
import com.cefire.cefiretlx.blog.dto.BlogDetailResponseDto;
import com.cefire.cefiretlx.blog.dto.BlogSummaryResponseDto;
import com.cefire.cefiretlx.blog.dto.BlogUpdateRequestDto;
import com.cefire.cefiretlx.blog.service.IBlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Tag(name = "Blogs", description = "Operaciones relacionadas con la gestión de Blogs")
public class BlogController {

  private final IBlogService blogService;
  private static final Logger logger = LoggerFactory.getLogger(BlogController.class);

  @GetMapping
  @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
  @Operation(summary = "Listar blogs paginados", description = "Retorna una página de blogs con filtros opcionales por título, estado, categoría y rango de fechas")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
      @ApiResponse(responseCode = "403", description = "Acceso denegado")
  })
  ResponseEntity<Page<BlogSummaryResponseDto>> findAllPaginated(
      @RequestParam(required = false) String title,
      @RequestParam(required = false) BlogStatus status,
      @RequestParam(required = false) Long categoryId,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime updatedFrom,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime updatedTo,
      @PageableDefault(page = 0, size = 10, sort = "updatedAt") Pageable pageable) {

    logger.info("Recibida solicitud GET /blogs con filtros");
    Page<BlogSummaryResponseDto> responseDto = blogService.findAllPaginated(title, status, categoryId, updatedFrom, updatedTo, pageable);
    logger.debug("Devolviendo {} eventos paginados", responseDto.getTotalElements());

    return ResponseEntity.ok(responseDto);
  }

  @GetMapping("/all")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Listar todos los blogs", description = "Retorna la lista completa de blogs sin paginación. Solo accesible para administradores")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
      @ApiResponse(responseCode = "403", description = "Acceso denegado")
  })
  ResponseEntity<List<BlogDetailResponseDto>> findAll() {
    logger.info("Recibida solicitud GET /blogs/all");
    List<BlogDetailResponseDto> result = blogService.findAll();
    logger.debug("Devolviendo {} blogs", result.size());
    return ResponseEntity.ok(result);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
  @Operation(summary = "Obtener blog por ID", description = "Retorna el detalle completo de un blog por su ID. El editor solo puede acceder a sus propios blogs")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Blog encontrado exitosamente"),
      @ApiResponse(responseCode = "403", description = "Acceso denegado"),
      @ApiResponse(responseCode = "404", description = "Blog no encontrado")
  })
  ResponseEntity<BlogDetailResponseDto> findById(@PathVariable Long id) {
    logger.info("Recibida solicitud GET /blogs/{}", id);
    BlogDetailResponseDto result = blogService.findById(id);
    logger.debug("Blog encontrado: {}", result.getTitle());
    return ResponseEntity.ok(result);
  }

  @GetMapping("/slug/{slug}")
  @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
  @Operation(summary = "Obtener blog por slug", description = "Retorna el detalle completo de un blog por su slug. El editor solo puede acceder a sus propios blogs")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Blog encontrado exitosamente"),
      @ApiResponse(responseCode = "403", description = "Acceso denegado"),
      @ApiResponse(responseCode = "404", description = "Blog no encontrado")
  })
  ResponseEntity<BlogDetailResponseDto> findBySlug(@PathVariable String slug) {
    logger.info("Recibida solicitud GET /blogs/slug/{}", slug);
    BlogDetailResponseDto result = blogService.findBySlug(slug);
    logger.debug("Blog encontrado por slug: {}", result.getTitle());
    return ResponseEntity.ok(result);
  }

  @GetMapping("/status/{status}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Listar blogs por estado", description = "Retorna todos los blogs que coincidan con el estado indicado (PUBLISHED o DRAFT)")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
      @ApiResponse(responseCode = "403", description = "Acceso denegado")
  })
  ResponseEntity<List<BlogDetailResponseDto>> findByStatus(@PathVariable BlogStatus status) {
    logger.info("Recibida solicitud GET /blogs/status/{}", status);
    List<BlogDetailResponseDto> result = blogService.findByStatus(status);
    logger.debug("Devolviendo {} blogs con estado {}", result.size(), status);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/category/{categoryId}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Listar blogs por categoría", description = "Retorna todos los blogs que pertenezcan a la categoría indicada")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
      @ApiResponse(responseCode = "403", description = "Acceso denegado"),
      @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
  })
  ResponseEntity<List<BlogDetailResponseDto>> findByCategoryId(@PathVariable Long categoryId) {
    logger.info("Recibida solicitud GET /blogs/category/{}", categoryId);
    List<BlogDetailResponseDto> result = blogService.findByCategoryId(categoryId);
    logger.debug("Devolviendo {} blogs de la categoría {}", result.size(), categoryId);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/author/{authorId}")
  @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
  @Operation(summary = "Listar blogs por autor", description = "Retorna todos los blogs de un autor. El editor solo puede consultar sus propios blogs")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
      @ApiResponse(responseCode = "403", description = "Acceso denegado")
  })
  ResponseEntity<List<BlogDetailResponseDto>> findByAuthorId(@PathVariable UUID authorId) {
    logger.info("Recibida solicitud GET /blogs/author/{}", authorId);
    List<BlogDetailResponseDto> result = blogService.findByAuthorId(authorId);
    logger.debug("Devolviendo {} blogs del autor {}", result.size(), authorId);
    return ResponseEntity.ok(result);
  }

  @PostMapping
  @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
  @Operation(summary = "Crear blog", description = "Crea un nuevo blog. El editor queda registrado automáticamente como autor")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Blog creado exitosamente"),
      @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
      @ApiResponse(responseCode = "403", description = "Acceso denegado"),
      @ApiResponse(responseCode = "409", description = "Ya existe un blog con ese slug")
  })
  ResponseEntity<BlogDetailResponseDto> create(@Valid @RequestBody BlogCreateRequestDto dto) {
    logger.info("Recibida solicitud para crear un blog: {}", dto.getTitle());
    BlogDetailResponseDto responseDto = blogService.create(dto);
    logger.debug("Blog creado exitosamente.. {}", responseDto.getTitle());
    return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
  }

  @PatchMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
  @Operation(summary = "Actualizar blog", description = "Actualiza parcialmente los campos de un blog. El editor solo puede modificar sus propios blogs")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Blog actualizado exitosamente"),
      @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
      @ApiResponse(responseCode = "403", description = "Acceso denegado"),
      @ApiResponse(responseCode = "404", description = "Blog no encontrado"),
      @ApiResponse(responseCode = "409", description = "Ya existe un blog con ese slug")
  })
  ResponseEntity<BlogDetailResponseDto> update(@PathVariable Long id, @Valid @RequestBody BlogUpdateRequestDto dto) {
    logger.info("Recibida solicitud PATCH /blogs/{}", id);
    BlogDetailResponseDto result = blogService.update(id, dto);
    logger.debug("Blog actualizado exitosamente: {}", result.getTitle());
    return ResponseEntity.ok(result);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Eliminar blog", description = "Elimina permanentemente un blog por su ID. Solo accesible para administradores")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Blog eliminado exitosamente"),
      @ApiResponse(responseCode = "403", description = "Acceso denegado"),
      @ApiResponse(responseCode = "404", description = "Blog no encontrado")
  })
  ResponseEntity<Void> delete(@PathVariable Long id) {
    logger.info("Recibida solicitud DELETE /blogs/{}", id);
    blogService.delete(id);
    logger.debug("Blog con id {} eliminado exitosamente", id);
    return ResponseEntity.noContent().build();
  }

}
