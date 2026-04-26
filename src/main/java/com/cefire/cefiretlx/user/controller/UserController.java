package com.cefire.cefiretlx.user.controller;

import com.cefire.cefiretlx.user.dto.UserDetailResponseDto;
import com.cefire.cefiretlx.user.dto.UserPageResponseDto;
import com.cefire.cefiretlx.user.dto.UserRequestDto;
import com.cefire.cefiretlx.user.dto.UserSummaryResponseDto;
import com.cefire.cefiretlx.user.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Operaciones relacionadas con la gestión de usuarios")
public class UserController {

  private final IUserService userService;

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Listar usuarios", description = "Retorna la lista completa de usuarios registrados en el sistema")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
      @ApiResponse(responseCode = "403", description = "Acceso denegado")
  })
  ResponseEntity<List<UserSummaryResponseDto>> getAllUsers() {
    return ResponseEntity.ok(userService.findAll());
  }

  @GetMapping("/paged")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Listar usuarios paginados", description = "Retorna una página de usuarios ordenada por apellido")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Página obtenida exitosamente"),
      @ApiResponse(responseCode = "403", description = "Acceso denegado")
  })
  ResponseEntity<Page<UserPageResponseDto>> getUsersPaged(
      @PageableDefault(size = 10, sort = "firstLastName") Pageable pageable) {
    return ResponseEntity.ok(userService.findAllPaged(pageable));
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Obtener usuario por ID", description = "Retorna el detalle completo de un usuario por su UUID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente"),
      @ApiResponse(responseCode = "403", description = "Acceso denegado"),
      @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
  })
  ResponseEntity<UserDetailResponseDto> getUserById(@PathVariable UUID id) {
    return ResponseEntity.ok(userService.findById(id));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario existente por su UUID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente"),
      @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
      @ApiResponse(responseCode = "403", description = "Acceso denegado"),
      @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
  })
  ResponseEntity<UserDetailResponseDto> updateUser(@PathVariable UUID id, @Valid @RequestBody UserRequestDto userRequestDto) {
    return ResponseEntity.ok(userService.update(id, userRequestDto));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Eliminar usuario", description = "Elimina permanentemente un usuario por su UUID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente"),
      @ApiResponse(responseCode = "403", description = "Acceso denegado"),
      @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
  })
  ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
    userService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{id}/roles/{roleName}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Asignar rol a usuario", description = "Agrega un rol al conjunto de roles de un usuario")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Rol asignado exitosamente"),
      @ApiResponse(responseCode = "403", description = "Acceso denegado"),
      @ApiResponse(responseCode = "404", description = "Usuario o rol no encontrado")
  })
  ResponseEntity<Void> assignRole(@PathVariable UUID id, @PathVariable String roleName) {
    userService.assignRole(id, roleName);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}/roles/{roleName}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(summary = "Remover rol de usuario", description = "Elimina un rol del conjunto de roles de un usuario")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Rol removido exitosamente"),
      @ApiResponse(responseCode = "403", description = "Acceso denegado"),
      @ApiResponse(responseCode = "404", description = "Usuario o rol no encontrado")
  })
  ResponseEntity<Void> removeRole(@PathVariable UUID id, @PathVariable String roleName) {
    userService.removeRole(id, roleName);
    return ResponseEntity.noContent().build();
  }

}
