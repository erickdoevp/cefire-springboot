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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public UserDetailResponseDto register(UserRequestDto userRequestDto) {
    if (userRepository.existsByUsername(userRequestDto.getUsername())) {
      throw new IllegalArgumentException("El username '" + userRequestDto.getUsername() + "' ya está en uso.");
    }
    if (userRepository.existsByEmail(userRequestDto.getEmail())) {
      throw new IllegalArgumentException("El email '" + userRequestDto.getEmail() + "' ya está registrado.");
    }
    User user = userMapper.toEntity(userRequestDto);
    user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
    return userMapper.toResponseDto(userRepository.save(user));
  }

  @Override
  @Transactional(readOnly = true)
  public UserDetailResponseDto findByUsername(String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + username));
    return userMapper.toResponseDto(user);
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserSummaryResponseDto> findAll() {
    return userMapper.toSummaryResponseDtoList(userRepository.findAll());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<UserPageResponseDto> findAllPaged(Pageable pageable) {
    return userRepository.findAll(pageable).map(userMapper::toPageResponseDto);
  }

  @Override
  @Transactional(readOnly = true)
  public UserDetailResponseDto findById(UUID id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el id " + id));
    return userMapper.toResponseDto(user);
  }

  @Override
  @Transactional
  public UserDetailResponseDto update(UUID id, UserRequestDto userRequestDto) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el id " + id));
    userMapper.updateUserFromDto(userRequestDto, user);
    return userMapper.toResponseDto(userRepository.save(user));
  }

  @Override
  @Transactional
  public void delete(UUID id) {
    if (!userRepository.existsById(id)) {
      throw new ResourceNotFoundException("Usuario no encontrado con el id " + id);
    }
    userRepository.deleteById(id);
  }

  @Override
  @Transactional
  public void assignRole(UUID id, String roleName) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el id " + id));
    Role role = roleRepository.findByName(roleName)
        .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado: " + roleName));
    user.getRoles().add(role);
    userRepository.save(user);
  }

  @Override
  @Transactional
  public void removeRole(UUID id, String roleName) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el id " + id));
    Role role = roleRepository.findByName(roleName)
        .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado: " + roleName));
    user.getRoles().remove(role);
    userRepository.save(user);
  }

}
