package com.shgxzybaba.testsolution.model;

import com.shgxzybaba.testsolution.enums.Role;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
public class User {

    @Id
    private long id;

    private String title;

    private String firstname;
    private String lastname;
    private String email;
    private String mobile;
    private String password;
    private Role role;
    private LocalDateTime dateRegistered;

}
