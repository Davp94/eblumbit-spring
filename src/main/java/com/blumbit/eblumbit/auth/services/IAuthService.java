package com.blumbit.eblumbit.auth.services;

import com.blumbit.eblumbit.auth.dto.AuthRequest;
import com.blumbit.eblumbit.auth.dto.AuthResponse;

public interface IAuthService {


    AuthResponse login(AuthRequest authRequest);

    AuthResponse refreshToken(String authentication);
}
