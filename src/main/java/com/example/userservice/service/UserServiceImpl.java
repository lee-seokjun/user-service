package com.example.userservice.service;

import com.example.userservice.client.OrderServiceClient;
import com.example.userservice.dto.UserDto;
import com.example.userservice.dto.UserUpdateDto;
import com.example.userservice.jpa.*;
import com.example.userservice.vo.ResponseOrder;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService{
    UserRepository userRepository;
    BCryptPasswordEncoder passwordEncoder;
    Environment env;
    OrderServiceClient orderServiceClient;

    CircuitBreakerFactory circuitBreakerFactory;
    UserRepositorySupport userRepositorySupport;
    RestTemplate restTemplate;

    @Autowired
    public UserServiceImpl(UserRepository  userRepository,  BCryptPasswordEncoder passwordEncoder,Environment env
            ,RestTemplate restTemplate
            ,OrderServiceClient orderServiceClient
            ,CircuitBreakerFactory circuitBreakerFactory
            ,UserRepositorySupport userRepositorySupport
            ){
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.env=env;
        this.orderServiceClient=orderServiceClient;
        this.circuitBreakerFactory=circuitBreakerFactory;
        this.userRepositorySupport = userRepositorySupport;

    }
    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        ModelMapper mapper =new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserEntity userEntity= mapper.map(userDto,UserEntity.class);
        userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));
        userRepository.save(userEntity);
        UserDto returnUserdto = UserDto.from(userEntity);
        return returnUserdto;
    }
    @Override
    public UserDto updateUser(String userId, UserUpdateDto userDto) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        userEntity.updateValue(userDto);

        userRepository.save(userEntity);
        UserDto returnUserdto = UserDto.from(userEntity);
        return returnUserdto;
    }
    @Override
    public UserDto getUserByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if(userEntity ==null)
            throw new UsernameNotFoundException("User Not found");
        UserDto userDto = UserDto.from(userEntity);

//        log.info("before call orders microservice");
//        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
//        List<ResponseOrder> orderList = circuitBreaker.run(() -> orderServiceClient.getOrders(userId),
//                throwable -> new ArrayList<>());
//        userDto.setOrders(orderList);
//        log.info("after call orders microservice");
        return userDto;
    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        UserDto userDto = new ModelMapper().map(userEntity,UserDto.class);
        if(userDto==null)
            throw new UsernameNotFoundException(email);

        return userDto;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //login에 사용
        UserEntity userEntity=userRepository.findByEmail(username);
        if(userEntity ==null)
        {
            throw new UsernameNotFoundException(username);
        }

        return new User(userEntity.getEmail(),userEntity.getEncryptedPwd(),true,true,true,true,new ArrayList<>());
    }

    @Override
    public List<UserDto> getUserInfos(List<String> userIds) {
        Iterable<UserEntity> userEntity  =userRepositorySupport.findByUserIds(userIds);
        List<UserDto> result = new ArrayList<>();
        userEntity.forEach(
                v->result.add(new ModelMapper().map(v,UserDto.class))
        );
        log.info("success");
        return result;
    }
    @Override
    public UserDto getUserInfo(String userId) {
        UserEntity userEntity  =userRepository.findByUserId(userId);
        log.info("success");
        return new ModelMapper().map(userEntity,UserDto.class);
    }


    @Override
    public boolean deleteUser(String userId) {
        UserEntity userEntity  =userRepository.findByUserId(userId);
        try {
            userRepository.deleteById(userEntity.getId());
            //제거 계정 토큰만료 추가해야함
            return true;
        }catch (Exception e)
        {
            log.error(e.getMessage());
        }
        return false;
        }


    @Override
    @Deprecated
    public UserDto getUserByUserIdUseRestTempalte(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if(userEntity ==null)
            throw new UsernameNotFoundException("User Not found");
        UserDto userDto = UserDto.from(userEntity);
        /** using as rest template*/

        String orderUrl = String.format(env.getProperty("order_service.url"),userId);
        ResponseEntity<List<ResponseOrder>> orderListResponse =
                restTemplate.exchange(orderUrl, HttpMethod.GET, null, new ParameterizedTypeReference<List<ResponseOrder>>() {
        });
        List<ResponseOrder> orderList = orderListResponse.getBody();

        try {
            orderList = orderServiceClient.getOrders(userId);
        }catch(FeignException fe)
        {
            log.error(fe.getMessage());
        }
//        userDto.setOrders(orderList);

        return userDto;
    }
}
