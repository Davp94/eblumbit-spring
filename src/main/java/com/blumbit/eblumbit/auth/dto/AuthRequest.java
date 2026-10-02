package com.blumbit.eblumbit.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor 
@Builder
public class AuthRequest {
    private String username;
    private String password;
}
