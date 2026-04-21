package com.cefire.cefiretlx.user.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserSummaryResponseDto {
  private UUID id;
  private String displayName;
}
