package com.cefire.cefiretlx.role.service;

import com.cefire.cefiretlx.role.domain.Role;
import com.cefire.cefiretlx.role.dto.RoleRequestDto;
import com.cefire.cefiretlx.role.dto.RoleResponseDto;
import com.cefire.cefiretlx.role.mapper.RoleMapper;
import com.cefire.cefiretlx.role.repository.RoleRepository;
import com.cefire.cefiretlx.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {

  private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

  private final RoleRepository roleRepository;
  private final RoleMapper roleMapper;

  @Override
  @Transactional
  public RoleResponseDto save(RoleRequestDto roleRequestDto) {
    logger.info("Creando rol con nombre='{}'", roleRequestDto.getName());
    Role role = roleMapper.toEntity(roleRequestDto);
    Role saved = roleRepository.save(role);
    logger.info("Rol creado exitosamente con id={}", saved.getId());
    return roleMapper.toResponseDto(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public List<RoleResponseDto> findAll() {
    logger.debug("Obteniendo todos los roles");
    List<RoleResponseDto> result = roleMapper.toResponseDtoList(roleRepository.findAll());
    logger.debug("findAll: {} roles encontrados", result.size());
    return result;
  }

  @Override
  @Transactional(readOnly = true)
  public RoleResponseDto findById(Long id) {
    logger.debug("Buscando rol por id={}", id);
    Role role = roleRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con el id " + id));
    return roleMapper.toResponseDto(role);
  }

  @Override
  @Transactional
  public RoleResponseDto update(Long id, RoleRequestDto roleRequestDto) {
    logger.info("Actualizando rol id={}", id);
    Role role = roleRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con el id " + id));
    roleMapper.updateRoleFromDto(roleRequestDto, role);
    Role saved = roleRepository.save(role);
    logger.info("Rol id={} actualizado a nombre='{}'", saved.getId(), saved.getName());
    return roleMapper.toResponseDto(saved);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    logger.info("Eliminando rol id={}", id);
    if (!roleRepository.existsById(id)) {
      logger.warn("Intento de eliminar rol inexistente con id={}", id);
      throw new ResourceNotFoundException("Rol no encontrado con el id " + id);
    }
    roleRepository.deleteById(id);
    logger.info("Rol id={} eliminado exitosamente", id);
  }

}
