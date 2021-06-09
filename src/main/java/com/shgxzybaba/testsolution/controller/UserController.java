package com.shgxzybaba.testsolution.controller;

import com.shgxzybaba.testsolution.exceptions.ApiException;
import com.shgxzybaba.testsolution.exceptions.InvalidDataException;
import com.shgxzybaba.testsolution.exceptions.UserNotFoundException;
import com.shgxzybaba.testsolution.model.apimodel.PageModel;
import com.shgxzybaba.testsolution.model.apimodel.UserApiModel;
import com.shgxzybaba.testsolution.model.entity.User;
import com.shgxzybaba.testsolution.service.DefaultUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final DefaultUserService userService;

    @Autowired
    public UserController(DefaultUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    @ResponseBody
    public void createUser(@RequestBody UserApiModel userApiModel) throws ApiException {
        try {
            userService.createUser(userApiModel);
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    }

    @PostMapping("/update")
    @ResponseBody
    public void updateUser(@RequestBody UserApiModel userApiModel) throws ApiException {
        try {
            userService.updateUser(userApiModel);
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    }

    @PostMapping("/deactivate")
    @ResponseBody
    public void deactivateUser(@RequestBody UserApiModel userApiModel) throws ApiException {
        try {
            userService.deleteUser(userApiModel);
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    }

    @PostMapping("/all")
    @ResponseBody
    public Page<User> getAllUsers(@RequestBody @Validated PageModel request) throws ApiException {
        try {
            return userService.getUsers(request);
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    }

    @GetMapping("/verify-email")
    @ResponseBody
    public void verifyUser(@RequestParam String email) throws ApiException {
        try {
            userService.verifyUser(email);
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    }
}
