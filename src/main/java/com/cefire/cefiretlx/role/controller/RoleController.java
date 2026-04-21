package com.cefire.cefiretlx.role.controller;

import com.cefire.cefiretlx.role.dto.RoleRequestDto;
import com.cefire.cefiretlx.role.dto.RoleResponseDto;
import com.cefire.cefiretlx.role.service.IRoleService;
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
public class RoleController {

  private final IRoleService roleService;

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  ResponseEntity<List<RoleResponseDto>> getAllRoles() {
    return ResponseEntity.ok(roleService.findAll());
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  ResponseEntity<RoleResponseDto> getRoleById(@PathVariable Long id) {
    return ResponseEntity.ok(roleService.findById(id));
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  ResponseEntity<RoleResponseDto> createRole(@Valid @RequestBody RoleRequestDto roleRequestDto) {
    return new ResponseEntity<>(roleService.save(roleRequestDto), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  ResponseEntity<RoleResponseDto> updateRole(@PathVariable Long id, @Valid @RequestBody RoleRequestDto roleRequestDto) {
    return ResponseEntity.ok(roleService.update(id, roleRequestDto));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  ResponseEntity<Void> deleteRole(@PathVariable Long id) {
    roleService.delete(id);
    return ResponseEntity.noContent().build();
  }

}
