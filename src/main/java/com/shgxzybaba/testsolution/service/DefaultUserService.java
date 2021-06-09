package com.shgxzybaba.testsolution.service;

import com.shgxzybaba.testsolution.enums.Status;
import com.shgxzybaba.testsolution.exceptions.InvalidDataException;
import com.shgxzybaba.testsolution.exceptions.UserNotFoundException;
import com.shgxzybaba.testsolution.model.apimodel.PageModel;
import com.shgxzybaba.testsolution.model.apimodel.UserApiModel;
import com.shgxzybaba.testsolution.model.entity.User;
import com.shgxzybaba.testsolution.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
public class DefaultUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final String emailContent;
    private final String emailSubject;
    private final String verificationUrl;
    private final String emailDeactivationSubject;
    private final String emailDeactivationContent;

    @Autowired
    public DefaultUserService(PasswordEncoder passwordEncoder,UserRepository userRepository, EmailService emailService,
                              @Value("${test.email.content}") String emailContent,
                              @Value("${test.email.subject}") String emailSubject,
                              @Value("${test.verification.url}") String verificationUrl,
                              @Value("${test.email.content}") String emailDeactivationContent,
                              @Value("${test.email.subject}") String emailDeactivationSubject
                              )
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.emailContent = emailContent;
        this.emailSubject = emailSubject;
        this.verificationUrl = verificationUrl;
        this.emailDeactivationContent = emailDeactivationContent;
        this.emailDeactivationSubject = emailDeactivationSubject;
    }

    
    public void createUser(UserApiModel request) throws InvalidDataException {

        if (StringUtils.isBlank(request.getEmail()) || StringUtils.isBlank(request.getPassword())) {
            throw new InvalidDataException("Invalid user creation request!");
        }

        User user = request.toUser();
        user.setDateRegistered(LocalDateTime.now());
        user.setStatus(Status.REGISTERED);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        sendVerificationMail(request.getEmail());
    }

    
    public Page<User> getUsers(PageModel page) {
        return userRepository.findByStatus(Status.VERIFIED, PageRequest.of(page.getPage(), page.getPageSize()));
    }

    
    public void updateUser(UserApiModel request) throws UserNotFoundException {
        User user = getUser(request.getEmail(), Status.VERIFIED);

        if (StringUtils.isNotBlank(request.getFirstname())) {
            user.setFirstname(request.getFirstname());
        }

        if (StringUtils.isNotBlank(request.getLastname())) {
            user.setLastname(request.getLastname());
        }

        if (StringUtils.isNotBlank(request.getMobile())) {
            user.setMobile(request.getMobile());
        }

        if (StringUtils.isNotBlank(request.getTitle())) {
            user.setTitle(request.getTitle());
        }

        userRepository.save(user);

    }

    
    public void deleteUser(UserApiModel request) throws UserNotFoundException {
        User user = getUser(request.getEmail(), Status.VERIFIED);
        user.setStatus(Status.DEACTIVATED);
        user.setDateDeactivated(LocalDateTime.now());
        userRepository.save(user);
        sendDeactivationMail(user.getEmail());
    }

    private void sendDeactivationMail(String email) {
        emailService.sendEmail(emailDeactivationContent, emailDeactivationSubject, email);
    }

    public void sendVerificationMail(String email) throws InvalidDataException {
        try {
            String encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8.toString());
            String finalContent = String.format(emailContent,verificationUrl, encodedEmail);
            emailService.sendEmail(finalContent, emailSubject, email);
        } catch (UnsupportedEncodingException e) {
            throw new InvalidDataException("An error occurred while sending verification email");
        }
    }

    public void verifyUser(String email) throws UserNotFoundException {
        User user = getUser(email, Status.REGISTERED);
        user.setStatus(Status.VERIFIED);
        user.setDateVerified(LocalDateTime.now());
        user.setVerified(true);
        userRepository.save(user);

    }

    private User getUser(String email, Status status) throws UserNotFoundException {
        User user = userRepository.findByEmailAndStatus(email, status);

        if (user == null) {
            throw new UserNotFoundException("Invalid email address supplied!");
        }

        return  user;
    }
}
