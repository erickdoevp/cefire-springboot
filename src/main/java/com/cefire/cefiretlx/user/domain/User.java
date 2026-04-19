package com.cefire.cefiretlx.user.domain;

import com.cefire.cefiretlx.role.domain.Role;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String firstLastName;
  private String secondLastName;
  private LocalDate createdAt;
  private String username;
  private String phoneNumber;
  private String email;
  private String password;
  private LocalDate lastSignInAt;
  private String avatarImgUrl;

  @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(
      name = "users_roles",
      joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")
  )
  private Set<Role> roles = new HashSet<>();

}
