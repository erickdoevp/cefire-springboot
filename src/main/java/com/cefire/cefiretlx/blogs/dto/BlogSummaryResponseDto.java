package com.cefire.cefiretlx.blogs.dto;

import com.cefire.cefiretlx.blogs.domain.BlogStatus;
import com.cefire.cefiretlx.category.dto.CategoryResponseDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BlogSummaryResponseDto {

  private Long id;
  private String title;
  private String authorDisplayName;
  private CategoryResponseDto category;
  private BlogStatus status;
  private LocalDateTime updatedAt;

}
