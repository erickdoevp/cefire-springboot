package com.cefire.cefiretlx.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class UserRequestDto {

  @NotBlank(message = "El nombre no debe estar vacío.")
  private String name;

  @NotBlank(message = "El primer apellido no debe estar vacío.")
  private String firstLastName;

  private String secondLastName;

  @NotBlank(message = "El email no debe estar vacío.")
  @Email(message = "El email no tiene un formato válido.")
  private String email;

  @NotBlank(message = "El username no debe estar vacío.")
  private String username;

  @NotBlank(message = "La contraseña no debe estar vacía.")
  private String password;

  private String phoneNumber;

  private String avatarImgUrl;

  private Set<String> roles;

}
