package com.cefire.cefiretlx.user.dto;

import com.cefire.cefiretlx.role.dto.RoleDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
public class UserDetailResponseDto {

  private UUID id;
  private String name;
  private String firstLastName;
  private String secondLastName;
  private String displayName;
  private String email;
  private String username;
  private String phoneNumber;
  private String avatarImgUrl;
  private LocalDateTime lastSignInAt;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Set<RoleDto> roles;

}
