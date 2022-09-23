package com.example.userservice.service;

import com.example.userservice.dto.UserDto;
import com.example.userservice.dto.UserUpdateDto;
import com.example.userservice.jpa.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto userDto);
    UserDto getUserByUserId(String userId);
    Iterable<UserEntity> getUserByAll();

    UserDto getUserDetailsByEmail(String username);
    UserDto updateUser(String userId, UserUpdateDto userDto);
    boolean deleteUser(String userId);
    List<UserDto> getUserInfos(List<String> userIds);
    UserDto getUserInfo(String userIds);
    UserDto getUserByUserIdUseRestTempalte(String userId);
}
