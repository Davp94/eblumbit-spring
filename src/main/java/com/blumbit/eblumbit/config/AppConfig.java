package com.blumbit.eblumbit.config;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.blumbit.eblumbit.entities.Usuario;
    import com.blumbit.eblumbit.repository.UsuarioRepository;

    @Configuration 
    public class AppConfig {

        @Autowired
        private UsuarioRepository usuarioRepository;

        //Configuration CORS
        @Bean
        public WebMvcConfigurer corsConfiguration() {
            return new WebMvcConfigurer() {
                @Override
                public void addCorsMappings(CorsRegistry registry) {
                    registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
                }
            };
        }

        @Bean 
        public UserDetailsService userDetailsService() {
            return username -> {
                Usuario usuario = usuarioRepository.findByUsername(username);
                if(usuario == null)
                    throw new RuntimeException("Usuario no encontrado");
                List<GrantedAuthority> authorities = usuario.getRolesUsuario().stream()
                    .flatMap(rolUsuario -> {
                        Stream.Builder<GrantedAuthority> builder = Stream.builder();
                        builder.add(new SimpleGrantedAuthority("ROLE_"+rolUsuario.getRol().getNombre()));
                        rolUsuario.getRol().getPermisos().stream().map(p->new SimpleGrantedAuthority(p.getNombre()))
                        .forEach(builder::add);
                        return builder.build();
                    })
                    .collect(Collectors.toList());
                return User.builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .authorities(authorities)
                .build();
            };
        }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean 
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) {
        return authConfig.getAuthenticationManager();
    }
}
