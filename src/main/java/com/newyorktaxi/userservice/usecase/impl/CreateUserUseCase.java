package com.newyorktaxi.userservice.usecase.impl;

import com.newyorktaxi.userservice.entity.User;
import com.newyorktaxi.userservice.mapper.UserMapper;
import com.newyorktaxi.userservice.repository.UserRepository;
import com.newyorktaxi.userservice.usecase.FunctionUseCase;
import com.newyorktaxi.userservice.usecase.params.UserParams;
import com.newyorktaxi.userservice.keycloak.KeycloakGateway;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CreateUserUseCase implements FunctionUseCase<UserParams, User> {

    UserRepository userRepository;
    UserMapper userMapper;
    KeycloakGateway keycloakUtil;

    @Override
    public User execute(UserParams params) {
        userRepository.findByUsername(params.getUsername())
                .ifPresent(user -> {
                    log.error("User already exists: {}", user.getUsername());
                    throw new DuplicateKeyException("User already exists");
                });

        final String userId = keycloakUtil.createKeycloakUser(params);
        keycloakUtil.setUserRole(userId, KeycloakGateway.ROLE_USER);

        final User user = userMapper.toUser(params);
        return userRepository.save(user);
    }

}
