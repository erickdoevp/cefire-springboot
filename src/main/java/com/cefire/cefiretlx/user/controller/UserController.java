package com.cefire.cefiretlx.user.controller;

import com.cefire.cefiretlx.user.dto.UserDetailResponseDto;
import com.cefire.cefiretlx.user.dto.UserPageResponseDto;
import com.cefire.cefiretlx.user.dto.UserRequestDto;
import com.cefire.cefiretlx.user.dto.UserSummaryResponseDto;
import com.cefire.cefiretlx.user.service.IUserService;
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
public class UserController {

  private final IUserService userService;

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  ResponseEntity<List<UserSummaryResponseDto>> getAllUsers() {
    return ResponseEntity.ok(userService.findAll());
  }

  @GetMapping("/paged")
  @PreAuthorize("hasRole('ADMIN')")
  ResponseEntity<Page<UserPageResponseDto>> getUsersPaged(
      @PageableDefault(size = 10, sort = "firstLastName") Pageable pageable) {
    return ResponseEntity.ok(userService.findAllPaged(pageable));
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  ResponseEntity<UserDetailResponseDto> getUserById(@PathVariable UUID id) {
    return ResponseEntity.ok(userService.findById(id));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  ResponseEntity<UserDetailResponseDto> updateUser(@PathVariable UUID id, @Valid @RequestBody UserRequestDto userRequestDto) {
    return ResponseEntity.ok(userService.update(id, userRequestDto));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
    userService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{id}/roles/{roleName}")
  @PreAuthorize("hasRole('ADMIN')")
  ResponseEntity<Void> assignRole(@PathVariable UUID id, @PathVariable String roleName) {
    userService.assignRole(id, roleName);
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/{id}/roles/{roleName}")
  @PreAuthorize("hasRole('ADMIN')")
  ResponseEntity<Void> removeRole(@PathVariable UUID id, @PathVariable String roleName) {
    userService.removeRole(id, roleName);
    return ResponseEntity.noContent().build();
  }

}
