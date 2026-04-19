package com.cefire.cefiretlx.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequestDto {

  @NotBlank(message = "El nombre de la categoría no debe estar vacía.")
  private String name;

  @NotBlank(message = "El color de la chip no debe estar vacío.")
  private String chipColor;

  @NotBlank(message = "El color del texto de la chip no debe estar vacío.")
  private String textChipColor;

}
