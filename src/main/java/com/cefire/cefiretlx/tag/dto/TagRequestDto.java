package com.cefire.cefiretlx.tag.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TagRequestDto {

  @NotBlank(message = "El nombre del tag no debe estar vacío.")
  private String name;

}
