package com.cefire.cefiretlx.role.service;

import com.cefire.cefiretlx.role.domain.Role;
import com.cefire.cefiretlx.role.dto.RoleRequestDto;
import com.cefire.cefiretlx.role.dto.RoleResponseDto;
import com.cefire.cefiretlx.role.mapper.RoleMapper;
import com.cefire.cefiretlx.role.repository.RoleRepository;
import com.cefire.cefiretlx.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {

  private final RoleRepository roleRepository;
  private final RoleMapper roleMapper;

  @Override
  @Transactional
  public RoleResponseDto save(RoleRequestDto roleRequestDto) {
    Role role = roleMapper.toEntity(roleRequestDto);
    return roleMapper.toResponseDto(roleRepository.save(role));
  }

  @Override
  @Transactional(readOnly = true)
  public List<RoleResponseDto> findAll() {
    return roleMapper.toResponseDtoList(roleRepository.findAll());
  }

  @Override
  @Transactional(readOnly = true)
  public RoleResponseDto findById(Long id) {
    Role role = roleRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con el id " + id));
    return roleMapper.toResponseDto(role);
  }

  @Override
  @Transactional
  public RoleResponseDto update(Long id, RoleRequestDto roleRequestDto) {
    Role role = roleRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con el id " + id));
    roleMapper.updateRoleFromDto(roleRequestDto, role);
    return roleMapper.toResponseDto(roleRepository.save(role));
  }

  @Override
  @Transactional
  public void delete(Long id) {
    if (!roleRepository.existsById(id)) {
      throw new ResourceNotFoundException("Rol no encontrado con el id " + id);
    }
    roleRepository.deleteById(id);
  }

}
