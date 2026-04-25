package com.cefire.cefiretlx.security.controller;

import com.cefire.cefiretlx.security.dto.JwtAuthResponseDto;
import com.cefire.cefiretlx.security.dto.LoginDto;
import com.cefire.cefiretlx.security.jwt.JwtGenerator;
import com.cefire.cefiretlx.user.dto.UserDetailResponseDto;
import com.cefire.cefiretlx.user.dto.UserRequestDto;
import com.cefire.cefiretlx.user.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final JwtGenerator jwtGenerator;
  private final IUserService userService;

  @PostMapping("/login")
  public ResponseEntity<JwtAuthResponseDto> authenticatedUser(@RequestBody LoginDto loginDto) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
    );

    SecurityContextHolder.getContext().setAuthentication(authentication);

    String token = jwtGenerator.generateToken(authentication);

    return new ResponseEntity<>(new JwtAuthResponseDto(token), HttpStatus.OK);

  };

  @PostMapping("/register")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<UserDetailResponseDto> register(@Valid @RequestBody UserRequestDto userRequestDto) {
    return new ResponseEntity<>(userService.register(userRequestDto), HttpStatus.CREATED);
  }

  @GetMapping("/authenticate")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<UserDetailResponseDto> getAuthenticatedUser(Authentication authentication) {
    return ResponseEntity.ok(userService.findByUsername(authentication.getName()));
  }

}
