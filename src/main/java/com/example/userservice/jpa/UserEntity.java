package com.example.userservice.jpa;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

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
    private String UserId;

    @Column(nullable = false,length = 200,unique = true)
    private String encryptedPwd;

}
