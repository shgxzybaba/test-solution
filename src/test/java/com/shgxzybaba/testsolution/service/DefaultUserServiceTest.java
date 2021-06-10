package com.shgxzybaba.testsolution.service;

import com.shgxzybaba.testsolution.enums.Status;
import com.shgxzybaba.testsolution.exceptions.InvalidDataException;
import com.shgxzybaba.testsolution.exceptions.UserNotFoundException;
import com.shgxzybaba.testsolution.model.apimodel.PageModel;
import com.shgxzybaba.testsolution.model.apimodel.UserApiModel;
import com.shgxzybaba.testsolution.model.entity.User;
import com.shgxzybaba.testsolution.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class DefaultUserServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder encoder;
    @Mock
    EmailService emailService;

    @InjectMocks
    DefaultUserService userService;

    UserApiModel testModel;
    List<User> testUsers;

    @BeforeEach
    void setUp() {
        initializeUser();
        initializeUserList();
        userService.setEmailContent("xyz");
        userService.setEmailSubject("zyyy");
        userService.setVerificationUrl("sdsfsf%s%s");
        when(encoder.encode(anyString())).thenReturn("xyz");
        when(userRepository.save(any(User.class))).thenReturn(mock(User.class));

        when(userRepository.findByStatus(any(Status.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(testUsers));

        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());

    }

    private void initializeUserList() {
        testUsers = new ArrayList<>();
        testUsers.add(mock(User.class));
        testUsers.add(mock(User.class));
    }

    @Test
    void createUser() throws InvalidDataException {
        userService.createUser(testModel);
        verify(emailService).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void createUser_null_mail() throws InvalidDataException {
        testModel.setEmail(null);
        assertThrows(InvalidDataException.class, () -> userService.createUser(testModel));
    }

    @Test
    void getUsers() {
       Page<User> users = userService.getUsers(new PageModel(0, 4));
       Assertions.assertThat(users.getTotalElements())
               .isEqualTo(2L);

    }

    @Test
    void updateUser_email_non_existent() throws UserNotFoundException {
        when(userRepository.findByEmailAndStatus(anyString(), any(Status.class))).thenReturn(null);
        assertThrows(UserNotFoundException.class, () ->userService.updateUser(testModel));
    }

    @Test
    void updateUser() throws UserNotFoundException {
        when(userRepository.findByEmailAndStatus(anyString(), any(Status.class))).thenReturn(mock(User.class));
        assertDoesNotThrow( () -> userService.updateUser(testModel));
    }



    @Test
    void deleteUser() throws UserNotFoundException {
        User user = mock(User.class);
        when(userRepository.findByEmailAndStatus(anyString(), any(Status.class))).thenReturn(user);
        userService.deleteUser(testModel);
        verify(user).setStatus(Status.DEACTIVATED);

    }

    void initializeUser() {
        testModel = new UserApiModel();
        testModel.setTitle("ti");
        testModel.setMobile("xxx");
        testModel.setPassword("dfdfd");
        testModel.setEmail("xxux@hsdfxxxyt.com");
        testModel.setLastname("sdfd");
        testModel.setFirstname("dsfsfrst");
    }
}