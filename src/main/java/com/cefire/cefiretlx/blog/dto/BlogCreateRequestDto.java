package com.cefire.cefiretlx.blog.dto;

import com.cefire.cefiretlx.blog.domain.BlogStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class BlogCreateRequestDto {

  @NotBlank(message = "El título no debe estar vacío.")
  private String title;

  @NotBlank(message = "El slug no debe estar vacío.")
  private String slug;

  @NotBlank(message = "El extracto no debe estar vacío.")
  private String excerpt;

  @NotBlank(message = "El contenido no debe estar vacío.")
  private String content;

  @NotBlank(message = "La conclusión no debe estar vacía.")
  private String conclusion;

  @NotBlank(message = "La imagen destacada no debe estar vacía.")
  private String featuredImage;

  @NotBlank(message = "La meta descripción no debe estar vacía.")
  private String metaDescription;

  @NotNull(message = "El tiempo de lectura no debe ser nulo.")
  @Min(value = 1, message = "El tiempo de lectura debe ser al menos 1 minuto.")
  private Integer readingTime;

  @NotNull(message = "El estado no debe ser nulo.")
  private BlogStatus status;

  @NotNull(message = "La categoría no debe ser nula.")
  private Long categoryId;

  @NotNull(message = "El autor no debe ser nulo.")
  private UUID authorId;

  private List<Long> tagIds;

}
