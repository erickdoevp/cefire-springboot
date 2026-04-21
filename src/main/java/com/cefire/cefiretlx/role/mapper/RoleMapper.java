package com.cefire.cefiretlx.role.mapper;

import com.cefire.cefiretlx.role.domain.Role;
import com.cefire.cefiretlx.role.dto.RoleDto;
import com.cefire.cefiretlx.role.dto.RoleRequestDto;
import com.cefire.cefiretlx.role.dto.RoleResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {

  RoleDto toDto(Role role);

  Role toEntity(RoleDto roleDto);

  List<RoleDto> toDtoList(List<Role> roles);

  @Mapping(target = "id", ignore = true)
  Role toEntity(RoleRequestDto roleRequestDto);

  RoleResponseDto toResponseDto(Role role);

  List<RoleResponseDto> toResponseDtoList(List<Role> roles);

  @Mapping(target = "id", ignore = true)
  void updateRoleFromDto(RoleRequestDto dto, @MappingTarget Role role);

}
