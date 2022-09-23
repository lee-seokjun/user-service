package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
import com.example.userservice.dto.UserUpdateDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.service.UserService;
import com.example.userservice.vo.*;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private UserService userService;
    UserController(UserService userService){
        this.userService=userService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<ResponseUser>> getUsers(){
        Iterable<UserEntity> userList = userService.getUserByAll();
        List<ResponseUser> result = new ArrayList<>();
        userList.forEach(v->{
            result.add(new ModelMapper().map(v,ResponseUser.class));
        });
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    @PostMapping()
    public ResponseEntity createUser(@RequestBody RequestUser user){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto= mapper.map(user, UserDto.class);
        userService.createUser(userDto);
        ResponseUser responseUser= mapper.map(userDto,ResponseUser.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }
    @PutMapping()
    public ResponseEntity updateUser(@RequestHeader ("userId") String userId,@RequestBody RequestUser user){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserUpdateDto userDto= mapper.map(user, UserUpdateDto.class);
        userService.updateUser(userId,userDto);
        ResponseUser responseUser= mapper.map(userDto,ResponseUser.class);
        return ResponseEntity.status(HttpStatus.OK).body(responseUser);
    }
    @DeleteMapping()
    public ResponseEntity deleteUser(@RequestHeader ("userId") String userId){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        if(userService.deleteUser(userId))
        {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
        else
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @GetMapping()
    public ResponseEntity<ResponseUser> getUser(
            @RequestHeader ("userId") String userId
    ){
        UserDto user = userService.getUserByUserId(userId);
        ResponseUser result = new ModelMapper().map(user,ResponseUser.class);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    /**
     *  여러 userId를 받아, 해당 userId Info 반환
     * */
    @GetMapping("/{userIds}")
    public ResponseEntity<List<ResponseUserInfo>> getUserInfo(@PathVariable("userIds") List<String> userIds){
        List<UserDto> user = userService.getUserInfos(userIds);
        List<ResponseUserInfo> result = new ArrayList<>();
        user.forEach(v->result.add(new ModelMapper().map(v,ResponseUserInfo.class)));
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
