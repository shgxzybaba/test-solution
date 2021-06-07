package com.shgxzybaba.testsolution.service;

import com.shgxzybaba.testsolution.exceptions.InvalidDataException;
import com.shgxzybaba.testsolution.model.apimodel.PageModel;
import com.shgxzybaba.testsolution.model.apimodel.UserApiModel;
import com.shgxzybaba.testsolution.model.entity.User;
import org.springframework.data.domain.Page;

public interface UserService {

    void createUser(UserApiModel user) throws InvalidDataException;
    Page<User> getUsers(PageModel model);
    void updateUser(UserApiModel user) throws InvalidDataException;
    void deleteUser(UserApiModel user) throws InvalidDataException;
    void sendVerificationMail(UserApiModel userApiModel);
}
