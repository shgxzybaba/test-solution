package com.shgxzybaba.testsolution.model.apimodel;

import com.shgxzybaba.testsolution.enums.Role;
import com.shgxzybaba.testsolution.enums.Status;
import com.shgxzybaba.testsolution.model.entity.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

/**
 * API wrapper class for the User details
 * This class will be used to do the necessary formatting required before sharing the userdata to the client
 *
 */
@Data
public class UserApiModel {

    private String title;

    private String firstname;
    private String lastname;
    private String email;
    private String mobile;
    private boolean verified;
    private String password;

    private Role role;
    private Status status;
    private LocalDateTime dateRegistered;
    private LocalDateTime dateVerified;
    private LocalDateTime dateDeactivated;

    public User toUser() {
        User user = new User();
        BeanUtils.copyProperties(this, user);
        return user;
    }

    public UserApiModel fromUser(User user) {
        UserApiModel model = new UserApiModel();
        BeanUtils.copyProperties(user, model);
        return model;
    }

}
