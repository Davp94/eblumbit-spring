package com.blumbit.eblumbit.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blumbit.eblumbit.dto.CreateUsuarioDto;
import com.blumbit.eblumbit.dto.UsuarioDto;
import com.blumbit.eblumbit.entities.Rol;
import com.blumbit.eblumbit.entities.RolUsuario;
import com.blumbit.eblumbit.entities.Usuario;
import com.blumbit.eblumbit.exception.BusinessRuleException;
import com.blumbit.eblumbit.exception.ConfictException;
import com.blumbit.eblumbit.exception.ResourceNotFoundException;
import com.blumbit.eblumbit.repository.RolRepository;
import com.blumbit.eblumbit.repository.RolUsuarioRepository;
import com.blumbit.eblumbit.repository.RefreshTokenRepository;
import com.blumbit.eblumbit.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private static final String DEFAULT_PASSWORD = "Password123*";

    private final UsuarioRepository usuarioRepository;

    private final RolRepository rolRepository;

    private final RolUsuarioRepository rolUsuarioRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository,
            RolUsuarioRepository rolUsuarioRepository, RefreshTokenRepository refreshTokenRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.rolUsuarioRepository = rolUsuarioRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UsuarioDto> findAllUsuarios() {
        List<UsuarioDto> usuariosDto = new ArrayList<>();
        for (Usuario u : usuarioRepository.findAll()) {
            usuariosDto.add(UsuarioDto.fromEntityData(u));
        }
        return usuariosDto;
    }

    public UsuarioDto findUsuarioById(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
        return UsuarioDto.fromEntityData(usuario);
    }

    public UsuarioDto findUsuarioByUsername(String username) {
        return UsuarioDto.fromEntityData(usuarioRepository.findByUsername(username));
    }

    @Transactional
    public UsuarioDto createUsuario(CreateUsuarioDto dto) {
        String email = dto.resolveEmail();
        if (email == null || email.isBlank()) {
            throw new BusinessRuleException("El email es obligatorio");
        }

        if (usuarioRepository.findByUsername(dto.getUsername()) != null) {
            throw new ConfictException("El username o el email ya existen");
        }
        if (usuarioRepository.findByEmailIgnoreCase(email) != null) {
            throw new ConfictException("El username o el email ya existen");
        }

        var usuarioToSave = CreateUsuarioDto.toEntity(dto);
        String password = (dto.getPassword() == null || dto.getPassword().isBlank())
                ? DEFAULT_PASSWORD
                : dto.getPassword();
        usuarioToSave.setPassword(passwordEncoder.encode(password));

        Usuario saved = usuarioRepository.save(usuarioToSave);
        replaceRoles(saved, dto.getRolIds());

        return UsuarioDto.fromEntityData(usuarioRepository.findById(saved.getId()).orElse(saved));
    }

    @Transactional
    public UsuarioDto updateUsuario(Integer id, CreateUsuarioDto dto) {
        Usuario usuarioFinded = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));

        String email = dto.resolveEmail();
        if (email == null || email.isBlank()) {
            throw new BusinessRuleException("El email es obligatorio");
        }

        Usuario sameUsername = usuarioRepository.findByUsername(dto.getUsername());
        if (sameUsername != null && !sameUsername.getId().equals(id)) {
            throw new ConfictException("El username o el email ya existen");
        }
        Usuario sameEmail = usuarioRepository.findByEmailIgnoreCase(email);
        if (sameEmail != null && !sameEmail.getId().equals(id)) {
            throw new ConfictException("El username o el email ya existen");
        }

        usuarioFinded.setUsername(dto.getUsername());
        usuarioFinded.setEmail(email);
        usuarioFinded.setNombres(dto.getNombres());
        usuarioFinded.setApellidos(dto.getApellidos());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            usuarioFinded.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        usuarioRepository.save(usuarioFinded);

        if (dto.getRolIds() != null) {
            replaceRoles(usuarioFinded, dto.getRolIds());
        }

        return UsuarioDto.fromEntityData(usuarioRepository.findById(id).orElse(usuarioFinded));
    }

    @Transactional
    public void deleteUsuario(Integer id) {
        rolUsuarioRepository.deleteByUsuario_Id(id);
        refreshTokenRepository.deleteByUser_Id(id);
        usuarioRepository.deleteById(id);
    }

    public void logicalDeleteUsuario(Integer id) {
        Usuario usuarioFinded = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
        usuarioFinded.setEstado(false);
        usuarioRepository.save(usuarioFinded);
    }

    private void replaceRoles(Usuario usuario, List<Integer> rolIds) {
        rolUsuarioRepository.deleteByUsuario_Id(usuario.getId());

        if (rolIds == null || rolIds.isEmpty()) {
            usuario.setRolesUsuario(new ArrayList<>());
            return;
        }

        List<Integer> distinctIds = rolIds.stream().distinct().toList();
        List<Rol> roles = rolRepository.findAllById(distinctIds);
        if (roles.size() != distinctIds.size()) {
            throw new BusinessRuleException("Uno o más roles no existen");
        }

        List<RolUsuario> links = new ArrayList<>();
        for (Rol rol : roles) {
            RolUsuario link = new RolUsuario();
            link.setUsuario(usuario);
            link.setRol(rol);
            link.setCreatedAt(LocalDateTime.now());
            links.add(link);
        }
        rolUsuarioRepository.saveAll(links);
        usuario.setRolesUsuario(links);
    }
}
