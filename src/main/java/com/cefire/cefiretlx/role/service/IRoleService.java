package com.cefire.cefiretlx.role.service;

import com.cefire.cefiretlx.role.dto.RoleRequestDto;
import com.cefire.cefiretlx.role.dto.RoleResponseDto;

import java.util.List;

public interface IRoleService {
  RoleResponseDto save(RoleRequestDto roleRequestDto);
  List<RoleResponseDto> findAll();
  RoleResponseDto findById(Long id);
  RoleResponseDto update(Long id, RoleRequestDto roleRequestDto);
  void delete(Long id);
}
