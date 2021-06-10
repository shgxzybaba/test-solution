package com.shgxzybaba.testsolution.service;

import com.shgxzybaba.testsolution.enums.Status;
import com.shgxzybaba.testsolution.exceptions.InvalidDataException;
import com.shgxzybaba.testsolution.exceptions.UserNotFoundException;
import com.shgxzybaba.testsolution.model.apimodel.PageModel;
import com.shgxzybaba.testsolution.model.apimodel.UserApiModel;
import com.shgxzybaba.testsolution.model.entity.User;
import com.shgxzybaba.testsolution.repository.UserRepository;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class DefaultUserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder encoder;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private DefaultUserService userService;

    UserApiModel testModel;
    private List<User> testUsers;

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
    void createUserNullMail() {
        testModel.setEmail(null);
        assertThrows(InvalidDataException.class, () -> userService.createUser(testModel));
    }

    @Test
    void getUsers() {
       Page<User> users = userService.getUsers(new PageModel(0, 4));
       assertThat(users.getTotalElements())
               .isEqualTo(2L);

    }

    @Test
    void updateUserEmailNonExistent() {
        when(userRepository.findByEmailAndStatus(anyString(), any(Status.class))).thenReturn(null);
        assertThrows(UserNotFoundException.class, () ->userService.updateUser(1L, testModel));
    }

    @Test
    void updateUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(mock(User.class)));
        assertDoesNotThrow( () -> userService.updateUser(1L, testModel));
    }



    @Test
    void deleteUser() throws UserNotFoundException {
        User user = mock(User.class);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        userService.deleteUser(1L);
        verify(user).setStatus(Status.DEACTIVATED);

    }

    @Test
    void verifyUser() throws UserNotFoundException {
        User user = mock(User.class);
        when(userRepository.findByEmailAndStatus(anyString(), any(Status.class))).thenReturn(user);
        userService.verifyUser("xyzUser");
        verify(user).setStatus(Status.VERIFIED);

    }

    @Test
    void verifyUserUserNonExistent() {
        when(userRepository.findByEmailAndStatus(anyString(), any(Status.class))).thenReturn(null);
        assertThrows(UserNotFoundException.class, () ->userService.verifyUser("xyzUser"));

    }

    private void initializeUser() {
        testModel = new UserApiModel();
        testModel.setTitle("ti");
        testModel.setMobile("xxx");
        testModel.setPassword("dfdfd");
        testModel.setEmail("xxux@hsdfxxxyt.com");
        testModel.setLastname("sdfd");
        testModel.setFirstname("dsfsfrst");
    }
}