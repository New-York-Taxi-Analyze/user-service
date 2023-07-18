package com.newyorktaxi.userservice.usecase.params;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;

@Value
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserParams {

    String username;

    String email;

    String password;

    String firstName;

    String lastName;
}
