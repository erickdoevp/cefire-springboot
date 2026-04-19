package com.cefire.cefiretlx.user.mapper;

import com.cefire.cefiretlx.role.domain.Role;
import com.cefire.cefiretlx.role.repository.RoleRepository;
import com.cefire.cefiretlx.user.domain.User;
import com.cefire.cefiretlx.user.dto.UserRequestDto;
import com.cefire.cefiretlx.user.dto.UserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.BeanMapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

  @Autowired
  protected RoleRepository roleRepository;

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "password", ignore = true)
  @Mapping(target = "lastSignInAt", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoleNamesToRoles")
  public abstract User toEntity(UserRequestDto dto);

  @Mapping(target = "displayName", expression = "java(user.getDisplayName())")
  public abstract UserResponseDto toResponseDto(User user);

  public abstract List<UserResponseDto> toResponseDtoList(List<User> users);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "password", ignore = true)
  @Mapping(target = "lastSignInAt", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoleNamesToRoles")
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  public abstract void updateUserFromDto(UserRequestDto dto, @MappingTarget User user);

  @Named("mapRoleNamesToRoles")
  public Set<Role> mapRoleNamesToRoles(Set<String> roleNames) {
    if (roleNames == null || roleNames.isEmpty()) {
      return roleRepository.findByName("ROLE_USER")
          .map(Collections::singleton)
          .orElseThrow(() -> new RuntimeException("El rol ROLE_USER no existe en la base de datos."));
    }
    return roleNames.stream()
        .map(name -> roleRepository.findByName(name)
            .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + name)))
        .collect(Collectors.toSet());
  }

}
