package com.newyorktaxi.userservice.controller;

import com.newyorktaxi.userservice.entity.User;
import com.newyorktaxi.userservice.mapper.UserMapper;
import com.newyorktaxi.userservice.model.UserRequest;
import com.newyorktaxi.userservice.usecase.FunctionUseCase;
import com.newyorktaxi.userservice.usecase.params.UserParams;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserMapper userMapper;
    FunctionUseCase<UserParams, User> createUserUseCase;

    @PostMapping("/createUser")
    public User createUser(@RequestBody UserRequest request) {
        log.info("UserHandler.createUser: {}", request);
        final UserParams userParams = userMapper.toUserParams(request);
        final User createdUser = createUserUseCase.execute(userParams);

        log.debug("User created successfully: {}", createdUser);
        return createdUser;
    }
}
