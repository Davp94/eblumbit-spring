package com.blumbit.eblumbit.utils;

import java.math.BigDecimal;
import java.util.Random;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.blumbit.eblumbit.entities.Producto;
import com.blumbit.eblumbit.entities.Usuario;
import com.blumbit.eblumbit.repository.ClienteRepository;
import com.blumbit.eblumbit.repository.ProductoRepository;
import com.blumbit.eblumbit.repository.SucursalRepository;
import com.blumbit.eblumbit.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;

@Component 
@RequiredArgsConstructor 
public class DataSeeder implements ApplicationRunner{
    
    private final ClienteRepository clienteRepository;

    private final UsuarioRepository usuarioRepository;

    private final SucursalRepository susursalRepository;

    private final ProductoRepository productoRepository;
    
    @Override
    public void run(ApplicationArguments args) throws Exception {

        Faker faker = new Faker();
        Random random = new Random();

        Usuario usuario = Usuario.builder()
        .email("admin@test.com")
        .estado(true)
        .password("123456")
        .username("admin")
        .build();

        for(int i=0 ; i<9; i++){

        }

        usuarioRepository.save(usuario);

        for(int i = 0 ; i<1000;i++){
            Producto producto = Producto.builder()
            .descripcion(faker.commerce().material()+" "+faker.commerce().brand())
            .nombre(faker.commerce().productName())
            .codigoBarra(faker.commerce().promotionCode())
            .marca(faker.company().name())
            .precioVentaActual(BigDecimal.valueOf(1+random.nextDouble()*199))
            .imagen(faker.internet().image())
            .estado(true)
            .build();
        }
    }

}
