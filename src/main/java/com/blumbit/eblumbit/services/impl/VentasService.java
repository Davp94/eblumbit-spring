package com.blumbit.eblumbit.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.blumbit.eblumbit.dto.ventas.AnularVentaDto;
import com.blumbit.eblumbit.dto.ventas.DetalleVentaRequest;
import com.blumbit.eblumbit.dto.ventas.DetalleVentaResponse;
import com.blumbit.eblumbit.dto.ventas.VentasRequest;
import com.blumbit.eblumbit.dto.ventas.VentasResponse;
import com.blumbit.eblumbit.entities.Cliente;
import com.blumbit.eblumbit.entities.DetalleVenta;
import com.blumbit.eblumbit.entities.Inventario;
import com.blumbit.eblumbit.entities.Usuario;
import com.blumbit.eblumbit.entities.Venta;
import com.blumbit.eblumbit.repository.AlmacenRepository;
import com.blumbit.eblumbit.repository.ClienteRepository;
import com.blumbit.eblumbit.repository.DetalleVentaRepository;
import com.blumbit.eblumbit.repository.InventarioRepository;
import com.blumbit.eblumbit.repository.ProductoRepository;
import com.blumbit.eblumbit.repository.UsuarioRepository;
import com.blumbit.eblumbit.repository.VentasRepository;
import com.blumbit.eblumbit.services.spec.IVentasService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor 
@Service 
public class VentasService implements IVentasService{

    private final VentasRepository ventasRepository;

    private final UsuarioRepository usuarioRepository;

    private final InventarioRepository inventarioRepository;

    private final AlmacenRepository almacenRepository;

    private final ProductoRepository productoRepository;

    private final DetalleVentaRepository detalleVentaRepository;

    private final ClienteRepository clienteRepository;

    @Override
    public List<VentasResponse> findAllVentas() {
        return ventasRepository.findAll().stream().map(VentasResponse::fromEntity)
        .collect(Collectors.toList());
    }

    @Override
    public VentasResponse findVentaById(Integer id) {

        VentasResponse ventasResponse = VentasResponse.fromEntity(ventasRepository.findById(id).orElseThrow(() -> new RuntimeException("No se encuentra la venta solicitada")));
        ventasResponse.setDetalleVenta(detalleVentaRepository.findByVenta_Id(id)
        .stream().map(DetalleVentaResponse::fromEntity).collect(Collectors.toList()));
        return ventasResponse;
    }

    @Transactional 
    @Override
    public VentasResponse createVenta(VentasRequest ventasRequest) {
       //crear venta
       Cliente cliente = clienteRepository.findById(ventasRequest.getClienteId())
       .orElseThrow(()-> new RuntimeException("Cliente no encontrado"));
       Usuario usuario = usuarioRepository.findById(ventasRequest.getUsuarioId())
       .orElseThrow(()-> new RuntimeException("Usuario no encontrado"));
       Venta venta = VentasRequest.toEntity(ventasRequest);
       venta.setCliente(cliente);
       venta.setUsuario(usuario);
       venta = ventasRepository.save(venta);
       //crear detalleVenta -> valid stock
       List<DetalleVenta> detallesVentaCreated = new ArrayList<>();
       for(DetalleVentaRequest detalleVentaRequest: ventasRequest.getDetalleVenta()) {
            if(validStock(detalleVentaRequest))
            {
                DetalleVenta detalleVentaToCreate = DetalleVentaRequest.toEntity(detalleVentaRequest);
                detalleVentaToCreate.setAlmacen(almacenRepository.findById(detalleVentaRequest.getAlmacenId())
                .orElseThrow(()-> new RuntimeException("Almacen no encontrado")));
                detalleVentaToCreate.setProducto(productoRepository.findById(detalleVentaRequest.getProductoId())
                .orElseThrow(()-> new RuntimeException("Producto no encontrado")));
                detallesVentaCreated.add(detalleVentaRepository.save(detalleVentaToCreate));
            }
       }
       return VentasResponse.fromEntity(venta);
    }

    @Override
    public void anularVenta(Integer id, AnularVentaDto anularVentaDto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'anularVenta'");
    }

    private boolean validStock(DetalleVentaRequest detalle)
    {
        Inventario inventarioRetrieved = inventarioRepository.findByAlmacenIdAndProductoId(detalle.getAlmacenId(), detalle.getProductoId())
        .orElseThrow(()-> new RuntimeException("Inventario no encontrado"));

        boolean valid = detalle.getCantidad() 
        <= inventarioRetrieved.getCantidadActual();
        if(valid){
            inventarioRetrieved.setCantidadActual(inventarioRetrieved.getCantidadActual()-detalle.getCantidad());
            inventarioRepository.save(inventarioRetrieved);
            return true;
        }else {
            throw new RuntimeException("Inventario no valido");
        }
    }

}
