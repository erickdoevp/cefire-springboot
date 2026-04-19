package com.cefire.cefiretlx.role.mapper;

import com.cefire.cefiretlx.role.domain.Role;
import com.cefire.cefiretlx.role.dto.RoleDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {

  RoleDto toDto(Role role);

  Role toEntity(RoleDto roleDto);

  List<RoleDto> toDtoList(List<Role> roles);
}
