package com.cefire.cefiretlx.data;

import com.cefire.cefiretlx.role.domain.Role;
import com.cefire.cefiretlx.role.repository.RoleRepository;
import com.cefire.cefiretlx.user.domain.User;
import com.cefire.cefiretlx.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public void run(String... args) throws Exception {

    // --- LÓGICA EXISTENTE PARA ROLES Y USUARIOS ---
    Role adminRole = roleRepository.findByName("ROLE_ADMIN")
        .orElseGet( () -> {
          Role newRole = new Role();
          newRole.setName("ROLE_ADMIN");
          return roleRepository.save(newRole);
        });

    Role userRole = roleRepository.findByName("ROLE_USER")
        .orElseGet(() -> {
          Role newRole = new Role();
          newRole.setName("ROLE_USER");
          return roleRepository.save(newRole);
        });

    roleRepository.findByName("ROLE_PHYSIOTHERAPIST")
        .orElseGet(() -> {
          Role newRole = new Role();
          newRole.setName("ROLE_PHYSIOTHERAPIST");
          return roleRepository.save(newRole);
        });

      roleRepository.findByName("ROLE_EDITOR")
        .orElseGet(() -> {
          Role newRole = new Role();
          newRole.setName("ROLE_EDITOR");
          return roleRepository.save(newRole);
        });

    if(userRepository.findByUsername("admin").isEmpty()){
      User admin = new User();
      admin.setName("Erick");
      admin.setFirstLastName("Cruz");
      admin.setSecondLastName("Padilla");
      admin.setUsername("admin");
      admin.setEmail("admin@example.com");
      admin.setPassword(passwordEncoder.encode("admin1234"));

      Set<Role> adminRoles = new HashSet<>();
      adminRoles.add(adminRole);
      adminRoles.add(userRole);

      admin.setRoles(adminRoles);

      userRepository.save(admin);
      System.out.println("Usuario 'admin' creado.");
    }

    if (userRepository.findByUsername("user").isEmpty()) {
      User regularUser = new User();
      regularUser.setName("Usuario Normal");
      regularUser.setFirstLastName("Apellido");
      regularUser.setSecondLastName("Apellido 2");
      regularUser.setUsername("user");
      regularUser.setEmail("user@example.com");
      regularUser.setPassword(passwordEncoder.encode("123456"));

      Set<Role> userRoles = new HashSet<>();
      userRoles.add(userRole);
      regularUser.setRoles(userRoles);

      userRepository.save(regularUser);
      System.out.println("Usuario 'user' creado.");
    }



  }

}
