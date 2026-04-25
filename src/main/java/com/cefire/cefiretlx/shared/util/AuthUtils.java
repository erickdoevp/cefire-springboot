package com.cefire.cefiretlx.shared.util;

import com.cefire.cefiretlx.shared.exception.ResourceNotFoundException;
import com.cefire.cefiretlx.user.domain.User;
import com.cefire.cefiretlx.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthUtils {

  private final UserRepository userRepository;

  public User getCurrentUser() {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("Usuario autenticado no encontrado"));
  }

  // Retorna true cuando el usuario tiene ROLE_EDITOR pero NO ROLE_ADMIN
  public boolean isOnlyEditor() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    boolean hasEditor = auth.getAuthorities().stream()
        .anyMatch(a -> a.getAuthority().equals("ROLE_EDITOR"));
    boolean hasAdmin = auth.getAuthorities().stream()
        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    return hasEditor && !hasAdmin;
  }

}
