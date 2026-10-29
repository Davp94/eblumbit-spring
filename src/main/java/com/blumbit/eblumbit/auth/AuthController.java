package com.blumbit.eblumbit.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blumbit.eblumbit.auth.dto.AuthRequest;
import com.blumbit.eblumbit.auth.dto.AuthResponse;
import com.blumbit.eblumbit.auth.dto.RefreshTokenRequest;
import com.blumbit.eblumbit.auth.services.IAuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping({ "/api/auth", "/auth" })
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authLogin(@Valid @RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authService.login(authRequest));
    }

    /**
     * Contrato canonico: el refresh token viaja en el body.
     * Se mantiene el header Authorization como fallback para clientes antiguos.
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(
            @RequestBody(required = false) RefreshTokenRequest request,
            @RequestHeader(value = "Authorization", required = false) String authentication) {
        RefreshTokenRequest refreshRequest = request != null ? request : new RefreshTokenRequest();
        if ((refreshRequest.getRefreshToken() == null || refreshRequest.getRefreshToken().isBlank())
                && authentication != null && authentication.startsWith("Bearer ")) {
            refreshRequest.setRefreshToken(authentication.substring(7));
        }
        return ResponseEntity.ok(authService.refreshToken(refreshRequest));
    }
}
