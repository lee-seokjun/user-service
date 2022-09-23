package com.example.userservice.dto;

import com.example.userservice.jpa.UserEntity;
import com.example.userservice.vo.ResponseOrder;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.Date;
import java.util.List;

@Data
public class UserDto {
    private String email;
    private String name;
    private String pwd;
    private String userId;
    private Date createAt;

    private String encryptedPwd;

//    private List<ResponseOrder> orders;
    public static UserDto from(UserEntity userEntity){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = mapper.map(userEntity,UserDto.class);
        return userDto;
    }
}
