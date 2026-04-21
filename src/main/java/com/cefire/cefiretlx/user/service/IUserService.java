package com.cefire.cefiretlx.user.service;

import com.cefire.cefiretlx.user.dto.UserDetailResponseDto;
import com.cefire.cefiretlx.user.dto.UserPageResponseDto;
import com.cefire.cefiretlx.user.dto.UserRequestDto;
import com.cefire.cefiretlx.user.dto.UserSummaryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IUserService {
  UserDetailResponseDto register(UserRequestDto userRequestDto);
  UserDetailResponseDto findByUsername(String username);
  List<UserSummaryResponseDto> findAll();
  Page<UserPageResponseDto> findAllPaged(Pageable pageable);
  UserDetailResponseDto findById(UUID id);
  UserDetailResponseDto update(UUID id, UserRequestDto userRequestDto);
  void delete(UUID id);
  void assignRole(UUID id, String roleName);
  void removeRole(UUID id, String roleName);
}
