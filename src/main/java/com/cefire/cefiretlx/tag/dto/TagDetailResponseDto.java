package com.cefire.cefiretlx.tag.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TagDetailResponseDto {
  private Long id;
  private String name;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
