package com.example.userservice.jpa;

import com.example.userservice.dto.UserUpdateDto;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name="users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,length = 50,unique = true)
    private String email;
    @Column(nullable = false,length = 50)
    private String name;
    @Column(nullable = false,length = 50,unique = true)
    private String userId;

    @Column(nullable = false,length = 200,unique = true)
    private String encryptedPwd;

    public void updateValue(UserUpdateDto newData)
    {
        this.email = newData.getEmail();
        this.name = newData.getName();
    }
}
