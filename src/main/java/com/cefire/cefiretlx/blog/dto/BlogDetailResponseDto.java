package com.cefire.cefiretlx.blog.dto;

import com.cefire.cefiretlx.blog.domain.BlogStatus;
import com.cefire.cefiretlx.category.dto.CategoryDetailResponseDto;
import com.cefire.cefiretlx.tag.dto.TagResponseDto;
import com.cefire.cefiretlx.user.dto.UserSummaryResponseDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BlogDetailResponseDto {

  private Long id;
  private String title;
  private String slug;
  private String excerpt;
  private String content;
  private String conclusion;
  private String featuredImage;
  private String metaDescription;
  private Integer readingTime;
  private BlogStatus status;
  private List<TagResponseDto> tags;
  private CategoryDetailResponseDto category;
  private UserSummaryResponseDto author;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

}
