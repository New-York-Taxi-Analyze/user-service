package com.newyorktaxi.userservice.mapper;

import com.newyorktaxi.userservice.entity.User;
import com.newyorktaxi.userservice.model.UserRequest;
import com.newyorktaxi.userservice.usecase.params.UserParams;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {

    UserParams toUserParams(UserRequest user);

    @Mapping(target = "id", ignore = true)
    User toUser(UserParams userParams);
}
