package com.shgxzybaba.testsolution.service;

import com.shgxzybaba.testsolution.enums.Status;
import com.shgxzybaba.testsolution.exceptions.InvalidDataException;
import com.shgxzybaba.testsolution.model.apimodel.PageModel;
import com.shgxzybaba.testsolution.model.apimodel.UserApiModel;
import com.shgxzybaba.testsolution.model.entity.User;
import com.shgxzybaba.testsolution.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DefaultUserService implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DefaultUserService(PasswordEncoder passwordEncoder,UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }

    @Override
    public void createUser(UserApiModel request) throws InvalidDataException {

        if (StringUtils.isBlank(request.getEmail()) || StringUtils.isBlank(request.getPassword())) {
            throw new InvalidDataException("Invalid user creation request!");
        }

        User user = request.toUser();
        user.setDateRegistered(LocalDateTime.now());
        user.setStatus(Status.REGISTERED);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        sendVerificationMail(request);


    }

    @Override
    public Page<User> getUsers(PageModel page) {

        return userRepository.findByStatus(Status.VERIFIED, PageRequest.of(page.getPage(), page.getPageSize()));
    }

    @Override
    public void updateUser(UserApiModel user) {

    }

    @Override
    public void deleteUser(UserApiModel user) {

    }

    @Override
    public void sendVerificationMail(UserApiModel userApiModel) {

    }
}
