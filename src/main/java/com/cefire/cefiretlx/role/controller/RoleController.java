package com.cefire.cefiretlx.role.controller;

import com.cefire.cefiretlx.role.dto.RoleRequestDto;
import com.cefire.cefiretlx.role.dto.RoleResponseDto;
import com.cefire.cefiretlx.role.service.IRoleService;
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
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@Tag(name = "Roles", description = "Operaciones relacionadas con la gestión de roles del sistema")
public class RoleController {

  private final IRoleService roleService;

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Listar roles", description = "Retorna la lista completa de roles disponibles en el sistema")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
      @ApiResponse(responseCode = "403", description = "Acceso denegado")
  })
  ResponseEntity<List<RoleResponseDto>> getAllRoles() {
    return ResponseEntity.ok(roleService.findAll());
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Obtener rol por ID", description = "Retorna el detalle de un rol por su ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Rol encontrado exitosamente"),
      @ApiResponse(responseCode = "403", description = "Acceso denegado"),
      @ApiResponse(responseCode = "404", description = "Rol no encontrado")
  })
  ResponseEntity<RoleResponseDto> getRoleById(@PathVariable Long id) {
    return ResponseEntity.ok(roleService.findById(id));
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Crear rol", description = "Crea un nuevo rol en el sistema")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Rol creado exitosamente"),
      @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
      @ApiResponse(responseCode = "403", description = "Acceso denegado"),
      @ApiResponse(responseCode = "409", description = "Ya existe un rol con ese nombre")
  })
  ResponseEntity<RoleResponseDto> createRole(@Valid @RequestBody RoleRequestDto roleRequestDto) {
    return new ResponseEntity<>(roleService.save(roleRequestDto), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Actualizar rol", description = "Actualiza el nombre de un rol existente")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Rol actualizado exitosamente"),
      @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
      @ApiResponse(responseCode = "403", description = "Acceso denegado"),
      @ApiResponse(responseCode = "404", description = "Rol no encontrado")
  })
  ResponseEntity<RoleResponseDto> updateRole(@PathVariable Long id, @Valid @RequestBody RoleRequestDto roleRequestDto) {
    return ResponseEntity.ok(roleService.update(id, roleRequestDto));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Eliminar rol", description = "Elimina permanentemente un rol del sistema por su ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Rol eliminado exitosamente"),
      @ApiResponse(responseCode = "403", description = "Acceso denegado"),
      @ApiResponse(responseCode = "404", description = "Rol no encontrado")
  })
  ResponseEntity<Void> deleteRole(@PathVariable Long id) {
    roleService.delete(id);
    return ResponseEntity.noContent().build();
  }

}
