package com.shgxzybaba.testsolution.controller;

import com.shgxzybaba.testsolution.exceptions.InvalidDataException;
import com.shgxzybaba.testsolution.exceptions.UserNotFoundException;
import com.shgxzybaba.testsolution.model.apimodel.PageModel;
import com.shgxzybaba.testsolution.model.apimodel.UserApiModel;
import com.shgxzybaba.testsolution.model.entity.User;
import com.shgxzybaba.testsolution.service.DefaultUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public void createUser(@RequestBody UserApiModel userApiModel) throws InvalidDataException {
        userService.createUser(userApiModel);
    }

    @PostMapping("/update")
    @ResponseBody
    public void updateUser(@RequestBody UserApiModel userApiModel) throws UserNotFoundException {
        userService.updateUser(userApiModel);
    }

    @PostMapping("/deactivate")
    @ResponseBody
    public void deactivateUser(@RequestBody UserApiModel userApiModel) throws UserNotFoundException {
        userService.deleteUser(userApiModel);
    }

    @PostMapping("/all")
    @ResponseBody
    public Page<User> getAllUsers(@RequestBody PageModel request) {
        return userService.getUsers(request);
    }

    @GetMapping("/verify-email")
    @ResponseBody
    public void verifyUser(@RequestParam String email) throws UserNotFoundException {
        userService.verifyUser(email);
    }
}
