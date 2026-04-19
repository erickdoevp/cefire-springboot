package com.cefire.cefiretlx.security.dto;

import lombok.Data;

@Data
public class JwtAuthResponseDto {

  public String accessToken;
  public String tokenType = "Bearer ";

  public JwtAuthResponseDto(String  accessToken) {
    this.accessToken = accessToken;
  };

}
