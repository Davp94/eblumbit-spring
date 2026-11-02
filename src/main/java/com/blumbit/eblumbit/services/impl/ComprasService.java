package com.blumbit.eblumbit.services.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
import com.blumbit.eblumbit.exception.BusinessRuleException;
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

    private static final String ESTADO_RECIBIDA = "RECIBIDA";
    private static final String ESTADO_ANULADA = "ANULADA";

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
                .orElseThrow(() -> new ResourceNotFoundException("Compra", id));
        ComprasResponse response = ComprasResponse.fromEntity(compra);
        response.setDetalleCompra(detalleCompraRepository.findByCompra_Id(id).stream()
                .map(DetalleCompraResponse::fromEntity)
                .collect(Collectors.toList()));
        return response;
    }

    @Transactional
    @Override
    public ComprasResponse createCompra(ComprasRequest comprasRequest) {
        if (comprasRequest.getDetalleCompra() == null || comprasRequest.getDetalleCompra().isEmpty()) {
            throw new BusinessRuleException("La compra debe incluir al menos un producto.");
        }
        if (comprasRequest.getDetalleCompra().stream()
                .anyMatch(d -> d.getCantidad() == null || d.getCantidad() <= 0
                        || d.getPrecioUnitarioCompra() == null
                        || d.getPrecioUnitarioCompra().compareTo(BigDecimal.ZERO) < 0)) {
            throw new BusinessRuleException(
                    "Las cantidades deben ser mayores a cero y los precios no pueden ser negativos.");
        }

        Proveedor proveedor = proveedorRepository.findById(comprasRequest.getProveedorId())
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor", comprasRequest.getProveedorId()));
        Usuario usuario = usuarioRepository.findById(comprasRequest.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", comprasRequest.getUsuarioId()));

        for (DetalleCompraRequest detalle : comprasRequest.getDetalleCompra()) {
            updateInventory(detalle);
        }

        Compra compra = Compra.builder()
                .codigo(String.format("BLUMC-%05d", comprasRepository.count() + 1))
                .fecha(LocalDateTime.now(ZoneOffset.UTC))
                .proveedor(proveedor)
                .usuario(usuario)
                .descuentoTotal(comprasRequest.getDescuentoTotal())
                .estado(ESTADO_RECIBIDA)
                .detalle(comprasRequest.getDetalle())
                .observaciones(comprasRequest.getObservaciones())
                .build();
        compra = comprasRepository.save(compra);

        for (DetalleCompraRequest detalleRequest : comprasRequest.getDetalleCompra()) {
            Almacen almacen = almacenRepository.findById(detalleRequest.getAlmacenId())
                    .orElseThrow(() -> new ResourceNotFoundException("Almacen", detalleRequest.getAlmacenId()));
            Producto producto = productoRepository.findById(detalleRequest.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto", detalleRequest.getProductoId()));

            DetalleCompra detalleCompra = DetalleCompra.builder()
                    .compra(compra)
                    .almacen(almacen)
                    .producto(producto)
                    .cantidad(detalleRequest.getCantidad())
                    .precioUnitarioCompra(detalleRequest.getPrecioUnitarioCompra())
                    .observaciones(detalleRequest.getObservaciones())
                    .build();
            detalleCompraRepository.save(detalleCompra);
        }

        return ComprasResponse.fromEntity(compra);
    }

    @Transactional
    @Override
    public void anularCompra(Integer id) {
        Compra compra = comprasRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Compra", id));

        if (!ESTADO_RECIBIDA.equals(compra.getEstado())) {
            throw new BusinessRuleException("Solo se pueden anular compras recibidas.");
        }

        List<DetalleCompra> detalles = detalleCompraRepository.findByCompra_Id(id);
        for (DetalleCompra detalle : detalles) {
            Inventario inventario = inventarioRepository
                    .findByAlmacenIdAndProductoId(detalle.getAlmacen().getId(), detalle.getProducto().getId())
                    .orElse(null);
            if (inventario == null || inventario.getCantidadActual() < detalle.getCantidad()) {
                throw new BusinessRuleException(
                        "No se puede anular la compra: stock insuficiente para el producto "
                                + detalle.getProducto().getId() + " en el almacen "
                                + detalle.getAlmacen().getId() + ".");
            }
            inventario.setCantidadActual(inventario.getCantidadActual() - detalle.getCantidad());
            inventario.setFechaActualizacion(LocalDateTime.now());
            inventarioRepository.save(inventario);
        }

        compra.setEstado(ESTADO_ANULADA);
        comprasRepository.save(compra);
    }

    private void updateInventory(DetalleCompraRequest detalle) {
        Almacen almacen = almacenRepository.findById(detalle.getAlmacenId())
                .orElseThrow(() -> new ResourceNotFoundException("Almacen", detalle.getAlmacenId()));
        Producto producto = productoRepository.findById(detalle.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto", detalle.getProductoId()));

        Inventario inventario = inventarioRepository
                .findByAlmacenIdAndProductoId(almacen.getId(), producto.getId())
                .orElseGet(() -> Inventario.builder()
                        .almacen(almacen)
                        .producto(producto)
                        .cantidadActual(0)
                        .build());
        inventario.setCantidadActual(inventario.getCantidadActual() + detalle.getCantidad());
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
        compraReportData.put("total", detalle.stream()
                .map(d -> d.getPrecioUnitarioCompra().multiply(BigDecimal.valueOf(d.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        return pdfService.generatePdfReport("compra-report", compraReportData);
    }
}
