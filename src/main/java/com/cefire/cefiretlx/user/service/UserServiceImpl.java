package com.cefire.cefiretlx.user.service;

import com.cefire.cefiretlx.role.domain.Role;
import com.cefire.cefiretlx.role.repository.RoleRepository;
import com.cefire.cefiretlx.shared.exception.ResourceNotFoundException;
import com.cefire.cefiretlx.user.domain.User;
import com.cefire.cefiretlx.user.dto.UserDetailResponseDto;
import com.cefire.cefiretlx.user.dto.UserPageResponseDto;
import com.cefire.cefiretlx.user.dto.UserRequestDto;
import com.cefire.cefiretlx.user.dto.UserSummaryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.cefire.cefiretlx.user.mapper.UserMapper;
import com.cefire.cefiretlx.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

  private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public UserDetailResponseDto register(UserRequestDto userRequestDto) {
    logger.info("Registrando nuevo usuario con username='{}'", userRequestDto.getUsername());
    if (userRepository.existsByUsername(userRequestDto.getUsername())) {
      logger.warn("Username '{}' ya está en uso", userRequestDto.getUsername());
      throw new IllegalArgumentException("El username '" + userRequestDto.getUsername() + "' ya está en uso.");
    }
    if (userRepository.existsByEmail(userRequestDto.getEmail())) {
      logger.warn("Email '{}' ya está registrado", userRequestDto.getEmail());
      throw new IllegalArgumentException("El email '" + userRequestDto.getEmail() + "' ya está registrado.");
    }
    User user = userMapper.toEntity(userRequestDto);
    user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
    User saved = userRepository.save(user);
    logger.info("Usuario registrado exitosamente con id={} username='{}'", saved.getId(), saved.getUsername());
    return userMapper.toResponseDto(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public UserDetailResponseDto findByUsername(String username) {
    logger.debug("Buscando usuario por username='{}'", username);
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));
    return userMapper.toResponseDto(user);
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserSummaryResponseDto> findAll() {
    logger.debug("Obteniendo todos los usuarios");
    List<UserSummaryResponseDto> result = userMapper.toSummaryResponseDtoList(userRepository.findAll());
    logger.debug("findAll: {} usuarios encontrados", result.size());
    return result;
  }

  @Override
  @Transactional(readOnly = true)
  public Page<UserPageResponseDto> findAllPaged(Pageable pageable) {
    logger.debug("Obteniendo usuarios paginados (página {}, tamaño {})", pageable.getPageNumber(), pageable.getPageSize());
    Page<UserPageResponseDto> result = userRepository.findAll(pageable).map(userMapper::toPageResponseDto);
    logger.debug("findAllPaged: {} usuarios en página {}/{}", result.getNumberOfElements(), result.getNumber(), result.getTotalPages());
    return result;
  }

  @Override
  @Transactional(readOnly = true)
  public UserDetailResponseDto findById(UUID id) {
    logger.debug("Buscando usuario por id={}", id);
    User user = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el id " + id));
    return userMapper.toResponseDto(user);
  }

  @Override
  @Transactional
  public UserDetailResponseDto update(UUID id, UserRequestDto userRequestDto) {
    logger.info("Actualizando usuario id={}", id);
    User user = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el id " + id));
    userMapper.updateUserFromDto(userRequestDto, user);
    User saved = userRepository.save(user);
    logger.info("Usuario id={} actualizado exitosamente", saved.getId());
    return userMapper.toResponseDto(saved);
  }

  @Override
  @Transactional
  public void delete(UUID id) {
    logger.info("Eliminando usuario id={}", id);
    if (!userRepository.existsById(id)) {
      logger.warn("Intento de eliminar usuario inexistente con id={}", id);
      throw new ResourceNotFoundException("Usuario no encontrado con el id " + id);
    }
    userRepository.deleteById(id);
    logger.info("Usuario id={} eliminado exitosamente", id);
  }

  @Override
  @Transactional
  public void assignRole(UUID id, String roleName) {
    logger.info("Asignando rol '{}' al usuario id={}", roleName, id);
    User user = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el id " + id));
    Role role = roleRepository.findByName(roleName)
        .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado: " + roleName));
    user.getRoles().add(role);
    userRepository.save(user);
    logger.info("Rol '{}' asignado exitosamente al usuario '{}'", roleName, user.getUsername());
  }

  @Override
  @Transactional
  public void removeRole(UUID id, String roleName) {
    logger.info("Removiendo rol '{}' del usuario id={}", roleName, id);
    User user = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el id " + id));
    Role role = roleRepository.findByName(roleName)
        .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado: " + roleName));
    user.getRoles().remove(role);
    userRepository.save(user);
    logger.info("Rol '{}' removido exitosamente del usuario '{}'", roleName, user.getUsername());
  }

}
