package com.cefire.cefiretlx.user.dto;

import com.cefire.cefiretlx.role.dto.RoleDto;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class UserPageResponseDto {
  private UUID id;
  private String displayName;
  private String username;
  private String email;
  private String phoneNumber;
  private Set<RoleDto> roles;
}
