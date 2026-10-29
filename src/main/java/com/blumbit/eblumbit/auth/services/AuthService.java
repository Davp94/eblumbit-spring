package com.blumbit.eblumbit.auth.services;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blumbit.eblumbit.auth.dto.AuthRequest;
import com.blumbit.eblumbit.auth.dto.AuthResponse;
import com.blumbit.eblumbit.auth.dto.RefreshTokenRequest;
import com.blumbit.eblumbit.entities.Permiso;
import com.blumbit.eblumbit.entities.RefreshToken;
import com.blumbit.eblumbit.entities.RolUsuario;
import com.blumbit.eblumbit.entities.Usuario;
import com.blumbit.eblumbit.exception.AuthenticationException;
import com.blumbit.eblumbit.repository.RefreshTokenRepository;
import com.blumbit.eblumbit.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    @Value("${application.jwt.refresh-expiration}")
    private Long refreshExpiration;

    private final UsuarioRepository usuarioRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Override
    @Transactional
    public AuthResponse login(AuthRequest authRequest) {
        if (authRequest == null || authRequest.getPassword() == null
                || (isBlank(authRequest.getEmail()) && isBlank(authRequest.getUsername()))) {
            throw new AuthenticationException("Credenciales inválidas");
        }

        Usuario usuario = findUsuario(authRequest);
        if (usuario == null) {
            throw new AuthenticationException("Credenciales inválidas");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuario.getUsername(), authRequest.getPassword(), null));

        if (!usuario.isEstado()) {
            throw new AuthenticationException("Usuario inactivo");
        }

        List<String> roles = resolveRoles(usuario);
        List<String> permissions = resolvePermissions(usuario);

        String accessToken = jwtService.generateAccesToken(usuario, permissions);
        String refreshToken = generateRefreshTokenValue();
        refreshTokenRepository.save(buildRefreshToken(usuario, refreshToken));

        return buildResponse(usuario, accessToken, refreshToken, roles, permissions);
    }

    @Override
    @Transactional
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        if (request == null || isBlank(request.getRefreshToken())) {
            throw new AuthenticationException("Token de actualización inválido");
        }

        Optional<RefreshToken> storedOpt = refreshTokenRepository.findByToken(request.getRefreshToken());
        if (storedOpt.isEmpty()) {
            throw new AuthenticationException("Token de actualización inválido");
        }

        RefreshToken stored = storedOpt.get();
        if (!stored.isActivo() || stored.getExpires().isBefore(LocalDateTime.now())) {
            throw new AuthenticationException("Token de actualización inválido o expirado");
        }

        Usuario usuario = stored.getUser();
        if (usuario == null || !usuario.isEstado()) {
            throw new AuthenticationException("Usuario inactivo");
        }

        List<String> roles = resolveRoles(usuario);
        List<String> permissions = resolvePermissions(usuario);

        String accessToken = jwtService.generateAccesToken(usuario, permissions);
        String newRefreshToken = generateRefreshTokenValue();

        stored.setActivo(false);
        refreshTokenRepository.save(stored);
        refreshTokenRepository.save(buildRefreshToken(usuario, newRefreshToken));

        return buildResponse(usuario, accessToken, newRefreshToken, roles, permissions);
    }

    private Usuario findUsuario(AuthRequest request) {
        if (!isBlank(request.getEmail())) {
            return usuarioRepository.findByEmailIgnoreCase(request.getEmail().trim());
        }
        return usuarioRepository.findByUsername(request.getUsername().trim());
    }

    private AuthResponse buildResponse(Usuario usuario, String accessToken, String refreshToken,
            List<String> roles, List<String> permissions) {
        int expirationMinutes = jwtService.getExpirationMinutes();
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .usuarioId(usuario.getId())
                .identifier(usuario.getId())
                .expirationMinutes(expirationMinutes)
                .expiration((long) expirationMinutes * 60_000L)
                .email(usuario.getEmail())
                .roles(roles)
                .permissions(permissions)
                .build();
    }

    private RefreshToken buildRefreshToken(Usuario usuario, String token) {
        LocalDateTime now = LocalDateTime.now();
        return RefreshToken.builder()
                .token(token)
                .created(now)
                .expires(now.plusNanos(refreshExpiration * 1_000_000L))
                .activo(true)
                .user(usuario)
                .build();
    }

    private String generateRefreshTokenValue() {
        byte[] randomBytes = new byte[64];
        SECURE_RANDOM.nextBytes(randomBytes);
        return Base64.getEncoder().encodeToString(randomBytes);
    }

    private List<String> resolveRoles(Usuario usuario) {
        if (usuario.getRolesUsuario() == null) {
            return List.of();
        }
        return usuario.getRolesUsuario().stream()
                .map(RolUsuario::getRol)
                .filter(Objects::nonNull)
                .map(rol -> rol.getNombre())
                .distinct()
                .toList();
    }

    private List<String> resolvePermissions(Usuario usuario) {
        if (usuario.getRolesUsuario() == null) {
            return List.of();
        }
        return usuario.getRolesUsuario().stream()
                .map(RolUsuario::getRol)
                .filter(Objects::nonNull)
                .filter(rol -> rol.getPermisos() != null)
                .flatMap(rol -> rol.getPermisos().stream())
                .map(Permiso::getNombre)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
