package com.blumbit.eblumbit.auth.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.blumbit.eblumbit.auth.dto.AuthRequest;
import com.blumbit.eblumbit.auth.dto.AuthResponse;
import com.blumbit.eblumbit.entities.Usuario;
import com.blumbit.eblumbit.exception.AuthenticationException;
import com.blumbit.eblumbit.exception.DomainException;
import com.blumbit.eblumbit.exception.ResourceNotFoundException;
import com.blumbit.eblumbit.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class AuthService implements IAuthService{

    @Value("${application.jwt.expiration}")
    private Long expiration;

    @Value("${application.jwt.refresh-expiration}")
    private Long refreshExpiration;

    private final UsuarioRepository usuarioRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    @Override
    public AuthResponse login(AuthRequest authRequest) {
        authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword(), null));
        Usuario usuario = usuarioRepository.findByUsername(authRequest.getUsername());
        if(usuario == null){
            throw new ResourceNotFoundException("Usuario", authRequest.getUsername());
        }
        String accessToken = jwtService.generateAccesToken(usuario);
        String refreshToken = jwtService.generateRefreshToken(usuario);
       return AuthResponse.builder()
       .accessToken(accessToken)
       .refreshToken(refreshToken)
       .identifier(usuario.getId())
       .expiration(expiration)
       .build();
    }

    @Override
    public AuthResponse refreshToken(String authentication) {
        if(authentication == null || !authentication.startsWith("Bearer ")){
            throw new AuthenticationException("Token de autenticación inválido");
        }
        String token = authentication.substring(7);
        String username = jwtService.extractUsername(token);
        Usuario usuario = usuarioRepository.findByUsername(username);
        if(usuario == null){
            throw new ResourceNotFoundException("Usuario", username);
        }
        String accessToken = jwtService.generateAccesToken(usuario);
        return AuthResponse.builder()
        .accessToken(accessToken)
        .refreshToken(null)
        .identifier(usuario.getId())
        .expiration(expiration)
        .build();
    }

}
