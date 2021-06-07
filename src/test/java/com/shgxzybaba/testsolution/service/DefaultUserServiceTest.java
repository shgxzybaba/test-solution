package com.shgxzybaba.testsolution.service;

import com.shgxzybaba.testsolution.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class DefaultUserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    DefaultUserService userService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void createUser() {
    }

    @Test
    void getUsers() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }
}