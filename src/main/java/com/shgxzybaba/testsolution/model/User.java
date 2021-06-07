package com.shgxzybaba.testsolution.model;

import com.shgxzybaba.testsolution.enums.Role;
import com.shgxzybaba.testsolution.enums.Status;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateTimeConverter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;
    @Email(message = "Email supplied must be a valid email address i.e example@xyz.com")
    private String email;
    @NotBlank
    private String mobile;
    @NotBlank
    private String password;
    private boolean verified;

    @Enumerated(EnumType.STRING)
    private Role role;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime dateRegistered;
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime dateVerified;
    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime dateDeactivated;

}
