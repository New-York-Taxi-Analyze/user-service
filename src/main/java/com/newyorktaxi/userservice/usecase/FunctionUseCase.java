package com.newyorktaxi.userservice.usecase;

import jakarta.validation.Valid;

public interface FunctionUseCase<T, R> {

    R execute(@Valid T params);
}
