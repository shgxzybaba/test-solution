package com.shgxzybaba.testsolution.repository;

import com.shgxzybaba.testsolution.enums.Status;
import com.shgxzybaba.testsolution.model.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp(){
        User user = new User();
        user.setStatus(Status.REGISTERED);
        user.setEmail("xyz@mail.com");
        user.setPassword("mmcfdxxry");
        user.setVerified(true);
        user.setFirstname("sdfsfs");
        user.setLastname("ccvdre");
        user.setMobile("3344343434");

        entityManager.persistAndFlush(user);
    }

    @Test
    void findByStatus() {

        Page<User> users = userRepository.findByStatus(Status.REGISTERED, Pageable.ofSize(3));
        Assertions.assertThat(users.getTotalElements()).isEqualTo(1);
    }

    @Test
    void findByEmailAndStatus() {
        User user = userRepository.findByEmailAndStatus("xyz@mail.com", Status.REGISTERED);
        Assertions.assertThat(user).isNotNull();
    }
}