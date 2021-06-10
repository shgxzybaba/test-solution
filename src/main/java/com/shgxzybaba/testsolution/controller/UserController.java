package com.shgxzybaba.testsolution.controller;

import com.shgxzybaba.testsolution.exceptions.ApiException;
import com.shgxzybaba.testsolution.model.apimodel.PageModel;
import com.shgxzybaba.testsolution.model.apimodel.UserApiModel;
import com.shgxzybaba.testsolution.model.entity.User;
import com.shgxzybaba.testsolution.service.DefaultUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final DefaultUserService userService;

    @Autowired
    public UserController(DefaultUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    @ResponseBody
    public void createUser(@RequestBody UserApiModel userApiModel) throws ApiException {
        try {
            userService.createUser(userApiModel);
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    }

    @PutMapping("/user/{id}")
    @ResponseBody
    public void updateUser(@PathVariable long id, @RequestBody UserApiModel request) throws ApiException {
        try {
            userService.updateUser(id, request);
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    }

    @DeleteMapping("/user/{id}")
    @ResponseBody
    public void deactivateUser(@PathVariable long id) throws ApiException {
        try {
            userService.deleteUser(id);
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    }

    @GetMapping("/users")
    @ResponseBody
    public Page<User> getAllUsers(@RequestParam int page, @RequestParam int pageSize) throws ApiException {
        try {
            PageModel request = new PageModel(page, pageSize);
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
