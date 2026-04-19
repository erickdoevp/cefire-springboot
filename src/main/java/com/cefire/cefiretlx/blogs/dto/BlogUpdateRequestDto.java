package com.cefire.cefiretlx.blogs.dto;

import com.cefire.cefiretlx.blogs.domain.BlogStatus;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.util.List;

@Data
public class BlogUpdateRequestDto {

  private String title;
  private String slug;
  private String excerpt;
  private String content;
  private String conclusion;
  private String featuredImage;
  private String metaDescription;

  @Min(value = 1, message = "El tiempo de lectura debe ser al menos 1 minuto.")
  private Integer readingTime;

  private BlogStatus status;
  private Long categoryId;
  private List<String> tags;

}
