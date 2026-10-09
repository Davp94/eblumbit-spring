package com.blumbit.eblumbit.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.blumbit.eblumbit.entities.Almacen;
import com.blumbit.eblumbit.entities.Categorias;
import com.blumbit.eblumbit.entities.Cliente;
import com.blumbit.eblumbit.entities.Inventario;
import com.blumbit.eblumbit.entities.Permiso;
import com.blumbit.eblumbit.entities.Producto;
import com.blumbit.eblumbit.entities.Proveedor;
import com.blumbit.eblumbit.entities.Rol;
import com.blumbit.eblumbit.entities.RolUsuario;
import com.blumbit.eblumbit.entities.Sucursal;
import com.blumbit.eblumbit.entities.Usuario;
import jakarta.persistence.EntityManager;
import com.blumbit.eblumbit.repository.ClienteRepository;
import com.blumbit.eblumbit.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;

@Component
@RequiredArgsConstructor
public class DataSeeder implements ApplicationRunner {

    private final EntityManager entityManager;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String[] SUBJECTS = {
        "Usuario", "Rol", "Permiso", "Cliente", "Proveedor",
        "Sucursal", "Categorias", "Producto", "Almacen", "Inventario"
    };
    private static final String[] ACTIONS = { "read", "create", "update", "delete" };

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (usuarioRepository.findByUsername("admin") != null) {
            return;
        }

        Faker faker = new Faker();
        Random random = new Random();

        List<Permiso> permisos = crearPermisos();
        Rol adminRole = crearRol("ADMIN", "Acceso completo", permisos);
        Rol ventasRole = crearRol("VENTAS", "Operaciones de ventas", permisosVentas(permisos));
        Rol rrhhRole = crearRol("RRHH", "Gestion de usuarios", permisosRrhh(permisos));

        List<Sucursal> sucursales = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Sucursal sucursal = new Sucursal(null, faker.company().name(), faker.address().streetAddress(),
                faker.phoneNumber().phoneNumber(), faker.address().city());
            entityManager.persist(sucursal);
            sucursales.add(sucursal);
        }

        List<Almacen> almacenes = new ArrayList<>();
        for (Sucursal sucursal : sucursales) {
            for (int i = 1; i <= 2; i++) {
                Almacen almacen = Almacen.builder()
                    .nombre("Almacen " + i + " - " + sucursal.getNombre())
                    .codigo("ALM-" + sucursal.getId() + "-" + i)
                    .descripcion(faker.lorem().sentence())
                    .sucursal(sucursal)
                    .build();
                entityManager.persist(almacen);
                almacenes.add(almacen);
            }
        }

        List<Categorias> categorias = crearCategorias(faker);
        crearClientes(faker, random);
        crearProveedores(faker);
        crearUsuarios(faker, adminRole, ventasRole, rrhhRole);

        for (int i = 0; i < 1000; i++) {
            Producto producto = Producto.builder()
                .descripcion(faker.commerce().material() + " " + faker.commerce().brand())
                .nombre(faker.commerce().productName())
                .codigoBarra(faker.number().digits(13))
                .unidadMedida("unidad")
                .marca(faker.company().name())
                .precioVentaActual(BigDecimal.valueOf(1 + random.nextDouble() * 199))
                .stockMinimo(random.nextInt(1, 20))
                .imagen(faker.internet().image())
                .estado(true)
                .fechaRegistro(LocalDateTime.now())
                .categoria(categorias.get(random.nextInt(categorias.size())))
                .build();
            entityManager.persist(producto);

            Inventario inventario = Inventario.builder()
                .cantidadActual(random.nextInt(0, 101))
                .fechaActualizacion(LocalDateTime.now())
                .almacen(almacenes.get(random.nextInt(almacenes.size())))
                .producto(producto)
                .build();
            entityManager.persist(inventario);
        }
    }

    private List<Permiso> crearPermisos() {
        List<Permiso> permisos = new ArrayList<>();
        for (String subject : SUBJECTS) {
            for (String action : ACTIONS) {
                Permiso permiso = new Permiso();
                permiso.setNombre(action + " " + subject);
                permiso.setDescripcion("Permite " + action + " en " + subject);
                permiso.setSubject(subject);
                permiso.setAction(action);
                entityManager.persist(permiso);
                permisos.add(permiso);
            }
        }
        return permisos;
    }

    private Rol crearRol(String nombre, String descripcion, List<Permiso> permisos) {
        Rol rol = new Rol();
        rol.setNombre(nombre);
        rol.setDescripcion(descripcion);
        rol.setPermisos(new ArrayList<>(permisos));
        entityManager.persist(rol);
        return rol;
    }

    private List<Permiso> permisosVentas(List<Permiso> permisos) {
        return permisos.stream()
            .filter(permiso -> List.of("Cliente", "Producto", "Inventario").contains(permiso.getSubject())
                ? !"delete".equals(permiso.getAction())
                : "read".equals(permiso.getAction())
                    && List.of("Proveedor", "Sucursal", "Categorias", "Almacen").contains(permiso.getSubject()))
            .toList();
    }

    private List<Permiso> permisosRrhh(List<Permiso> permisos) {
        return permisos.stream()
            .filter(permiso -> "Usuario".equals(permiso.getSubject())
                ? !"delete".equals(permiso.getAction())
                : "read".equals(permiso.getAction()) && "Sucursal".equals(permiso.getSubject()))
            .toList();
    }

    private List<Categorias> crearCategorias(Faker faker) {
        String[] nombres = { "Electronica", "Hogar", "Oficina", "Alimentos", "Ropa", "Deportes", "Limpieza", "Juguetes" };
        List<Categorias> categorias = new ArrayList<>();
        for (String nombre : nombres) {
            Categorias categoria = new Categorias(nombre, faker.lorem().sentence());
            entityManager.persist(categoria);
            categorias.add(categoria);
        }
        return categorias;
    }

    private void crearClientes(Faker faker, Random random) {
        for (int i = 0; i < 50; i++) {
            Cliente cliente = Cliente.builder()
                .nombreCompleto(faker.name().fullName())
                .nroIdentificacion(faker.number().digits(10))
                .fechaNacimiento(LocalDate.now().minusYears(random.nextInt(18, 75)))
                .telefono(faker.phoneNumber().phoneNumber())
                .correo("cliente" + i + "@example.test")
                .estado(true)
                .build();
            clienteRepository.save(cliente);
        }
    }

    private void crearProveedores(Faker faker) {
        for (int i = 0; i < 20; i++) {
            Proveedor proveedor = Proveedor.builder()
                .razonSocial(faker.company().name())
                .nroIdentificacion(faker.number().digits(13))
                .contacto(faker.name().fullName())
                .telefono(faker.phoneNumber().phoneNumber())
                .correo("proveedor" + i + "@example.test")
                .observaciones(faker.lorem().sentence())
                .estado(true)
                .build();
            entityManager.persist(proveedor);
        }
    }

    private void crearUsuarios(Faker faker, Rol adminRole, Rol ventasRole, Rol rrhhRole) {
        crearUsuario("admin", "admin@test.com", passwordEncoder.encode("123456"), adminRole);
        for (int i = 1; i <= 9; i++) {
            Rol rol = i <= 5 ? ventasRole : rrhhRole;
            crearUsuario("usuario" + i, "usuario" + i + "@example.test", passwordEncoder.encode("123456"), rol);
        }
    }

    private void crearUsuario(String username, String email, String password, Rol rol) {
        Usuario usuario = Usuario.builder()
            .email(email)
            .estado(true)
            .password(password)
            .username(username)
            .build();
        entityManager.persist(usuario);

        RolUsuario rolUsuario = new RolUsuario();
        rolUsuario.setUsuario(usuario);
        rolUsuario.setRol(rol);
        rolUsuario.setCreatedAt(LocalDateTime.now());
        entityManager.persist(rolUsuario);
    }
}
