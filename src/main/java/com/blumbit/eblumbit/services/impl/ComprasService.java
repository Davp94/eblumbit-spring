package com.blumbit.eblumbit.services.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.blumbit.eblumbit.dto.compras.ComprasRequest;
import com.blumbit.eblumbit.dto.compras.ComprasResponse;
import com.blumbit.eblumbit.dto.compras.DetalleCompraRequest;
import com.blumbit.eblumbit.dto.compras.DetalleCompraResponse;
import com.blumbit.eblumbit.entities.Almacen;
import com.blumbit.eblumbit.entities.Compra;
import com.blumbit.eblumbit.entities.DetalleCompra;
import com.blumbit.eblumbit.entities.Inventario;
import com.blumbit.eblumbit.entities.Producto;
import com.blumbit.eblumbit.entities.Proveedor;
import com.blumbit.eblumbit.entities.Usuario;
import com.blumbit.eblumbit.exception.ResourceNotFoundException;
import com.blumbit.eblumbit.repository.AlmacenRepository;
import com.blumbit.eblumbit.repository.ComprasRepository;
import com.blumbit.eblumbit.repository.DetalleCompraRepository;
import com.blumbit.eblumbit.repository.InventarioRepository;
import com.blumbit.eblumbit.repository.ProductoRepository;
import com.blumbit.eblumbit.repository.ProveedorRepository;
import com.blumbit.eblumbit.repository.UsuarioRepository;
import com.blumbit.eblumbit.services.spec.IComprasService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ComprasService implements IComprasService {

    private final PdfService pdfService;
    private final ComprasRepository comprasRepository;
    private final ProveedorRepository proveedorRepository;
    private final UsuarioRepository usuarioRepository;
    private final DetalleCompraRepository detalleCompraRepository;
    private final AlmacenRepository almacenRepository;
    private final ProductoRepository productoRepository;
    private final InventarioRepository inventarioRepository;

    @Override
    public List<ComprasResponse> findAllCompras() {
        return comprasRepository.findAll().stream()
            .map(ComprasResponse::fromEntity)
            .collect(Collectors.toList());
    }

    @Override
    public ComprasResponse findCompraById(Integer id) {
        Compra compra = comprasRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No se encuentra la compra solicitada"));
        ComprasResponse response = ComprasResponse.fromEntity(compra);
        response.setDetalleCompra(detalleCompraRepository.findByCompra_Id(id).stream()
            .map(DetalleCompraResponse::fromEntity)
            .collect(Collectors.toList()));
        return response;
    }

    @Transactional
    @Override
    public ComprasResponse createCompra(ComprasRequest comprasRequest) {
        Proveedor proveedor = proveedorRepository.findById(comprasRequest.getProveedorId())
            .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        Usuario usuario = usuarioRepository.findById(comprasRequest.getUsuarioId())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Compra compra = Compra.builder()
            .codigo(comprasRequest.getCodigo())
            .fecha(comprasRequest.getFecha() == null ? LocalDateTime.now() : comprasRequest.getFecha())
            .proveedor(proveedor)
            .usuario(usuario)
            .descuentoTotal(comprasRequest.getDescuentoTotal())
            .estado(comprasRequest.getEstado())
            .detalle(comprasRequest.getDetalle())
            .observaciones(comprasRequest.getObservaciones())
            .build();
        compra = comprasRepository.save(compra);

        for (DetalleCompraRequest detalleRequest : comprasRequest.getDetalleCompra()) {
            Almacen almacen = almacenRepository.findById(detalleRequest.getAlmacenId())
                .orElseThrow(() -> new RuntimeException("Almacen no encontrado"));
            Producto producto = productoRepository.findById(detalleRequest.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            DetalleCompra detalleCompra = DetalleCompra.builder()
                .compra(compra)
                .almacen(almacen)
                .producto(producto)
                .cantidad(detalleRequest.getCantidad())
                .precioUnitarioCompra(detalleRequest.getPrecioUnitarioCompra())
                .observaciones(detalleRequest.getObservaciones())
                .build();
            detalleCompraRepository.save(detalleCompra);
            updateInventory(almacen, producto, detalleRequest.getCantidad());
        }

        return findCompraById(compra.getId());
    }

    private void updateInventory(Almacen almacen, Producto producto, Integer cantidad) {
        Inventario inventario = inventarioRepository
            .findByAlmacenIdAndProductoId(almacen.getId(), producto.getId())
            .orElseGet(() -> Inventario.builder()
                .almacen(almacen)
                .producto(producto)
                .cantidadActual(0)
                .build());
        inventario.setCantidadActual(inventario.getCantidadActual() + cantidad);
        inventario.setFechaActualizacion(LocalDateTime.now());
        inventarioRepository.save(inventario);
    }

    @Override
    public byte[] generateReport(Integer id) {
        Compra compraRetrieved = comprasRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Compra", id));
        List<DetalleCompra> detalle = detalleCompraRepository.findByCompra_Id(id);
        Map<String, Object> compraReportData = new HashMap<>();
        compraReportData.put("compra", compraRetrieved);
        compraReportData.put("detalleCompra", detalle);
        compraReportData.put("total", detalle.stream().map(d->d.getPrecioUnitarioCompra()
        .multiply(BigDecimal.valueOf(d.getCantidad()))).reduce(BigDecimal.ZERO, BigDecimal::add));

        return pdfService.generatePdfReport("compra-report", compraReportData);
    }
}