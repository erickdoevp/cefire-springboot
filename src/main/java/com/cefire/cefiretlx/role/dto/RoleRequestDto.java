package com.cefire.cefiretlx.role.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoleRequestDto {

  @NotBlank(message = "El nombre del rol no debe estar vacío.")
  private String name;

}
