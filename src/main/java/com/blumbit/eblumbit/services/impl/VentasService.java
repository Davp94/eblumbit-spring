package com.blumbit.eblumbit.services.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.blumbit.eblumbit.dto.ventas.DetalleVentaRequest;
import com.blumbit.eblumbit.dto.ventas.DetalleVentaResponse;
import com.blumbit.eblumbit.dto.ventas.VentasDetailResponse;
import com.blumbit.eblumbit.dto.ventas.VentasRequest;
import com.blumbit.eblumbit.dto.ventas.VentasResponse;
import com.blumbit.eblumbit.entities.Almacen;
import com.blumbit.eblumbit.entities.Cliente;
import com.blumbit.eblumbit.entities.DetalleVenta;
import com.blumbit.eblumbit.entities.Inventario;
import com.blumbit.eblumbit.entities.Producto;
import com.blumbit.eblumbit.entities.Usuario;
import com.blumbit.eblumbit.entities.Venta;
import com.blumbit.eblumbit.exception.BusinessRuleException;
import com.blumbit.eblumbit.exception.ResourceNotFoundException;
import com.blumbit.eblumbit.repository.AlmacenRepository;
import com.blumbit.eblumbit.repository.ClienteRepository;
import com.blumbit.eblumbit.repository.DetalleVentaRepository;
import com.blumbit.eblumbit.repository.InventarioRepository;
import com.blumbit.eblumbit.repository.ProductoRepository;
import com.blumbit.eblumbit.repository.UsuarioRepository;
import com.blumbit.eblumbit.repository.VentasRepository;
import com.blumbit.eblumbit.services.spec.IPdfService;
import com.blumbit.eblumbit.services.spec.IVentasService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class VentasService implements IVentasService {

    private final VentasRepository ventasRepository;
    private final UsuarioRepository usuarioRepository;
    private final InventarioRepository inventarioRepository;
    private final AlmacenRepository almacenRepository;
    private final ProductoRepository productoRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final ClienteRepository clienteRepository;
    private final IPdfService pdfService;

    @Override
    public List<VentasResponse> findAllVentas() {
        return ventasRepository.findAll().stream()
                .map(VentasResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public VentasDetailResponse findVentaById(Integer id) {
        Venta venta = ventasRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta", id));
        List<DetalleVentaResponse> detalle = detalleVentaRepository.findByVenta_Id(id).stream()
                .map(DetalleVentaResponse::fromEntity)
                .collect(Collectors.toList());
        return VentasDetailResponse.fromEntity(venta, detalle);
    }

    @Transactional
    @Override
    public VentasResponse createVenta(VentasRequest ventasRequest) {
        if (ventasRequest == null || ventasRequest.getDetalleVenta() == null
                || ventasRequest.getDetalleVenta().isEmpty()) {
            throw new BusinessRuleException("La venta debe incluir al menos un producto");
        }

        Cliente cliente = clienteRepository.findById(ventasRequest.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", ventasRequest.getClienteId()));
        Usuario usuario = usuarioRepository.findById(ventasRequest.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", ventasRequest.getUsuarioId()));

        for (DetalleVentaRequest detalle : ventasRequest.getDetalleVenta()) {
            validStockAndDiscount(detalle);
        }

        Venta venta = Venta.builder()
                .codigo(String.format("BLUMB-%05d", ventasRepository.count() + 1))
                .fecha(LocalDateTime.now(ZoneOffset.UTC))
                .descuentoTotal(ventasRequest.resolveDescuento())
                .detalle(ventasRequest.resolveObservacion())
                .estado(true)
                .cliente(cliente)
                .usuario(usuario)
                .build();
        venta = ventasRepository.save(venta);

        for (DetalleVentaRequest detalleRequest : ventasRequest.getDetalleVenta()) {
            Almacen almacen = almacenRepository.findById(detalleRequest.getAlmacenId())
                    .orElseThrow(() -> new ResourceNotFoundException("Almacen", detalleRequest.getAlmacenId()));
            Producto producto = productoRepository.findById(detalleRequest.getProductoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto", detalleRequest.getProductoId()));

            DetalleVenta detalleVenta = DetalleVenta.builder()
                    .venta(venta)
                    .almacen(almacen)
                    .producto(producto)
                    .cantidad(detalleRequest.getCantidad())
                    .precioUnitarioVenta(detalleRequest.getPrecioUnitarioVenta())
                    .observaciones(detalleRequest.resolveObservaciones())
                    .build();
            detalleVentaRepository.save(detalleVenta);
        }

        return VentasResponse.fromEntity(venta);
    }

    @Transactional
    @Override
    public void anularVenta(Integer id) {
        Venta venta = ventasRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta", id));

        if (!Boolean.TRUE.equals(venta.getEstado())) {
            throw new BusinessRuleException("La venta ya se encuentra anulada");
        }

        venta.setEstado(false);
        for (DetalleVenta detalle : detalleVentaRepository.findByVenta_Id(id)) {
            inventarioRepository
                    .findByAlmacenIdAndProductoId(detalle.getAlmacen().getId(), detalle.getProducto().getId())
                    .ifPresent(inventario -> {
                        inventario.setCantidadActual(inventario.getCantidadActual() + detalle.getCantidad());
                        inventario.setFechaActualizacion(LocalDateTime.now());
                        inventarioRepository.save(inventario);
                    });
        }
        ventasRepository.save(venta);
    }

    private void validStockAndDiscount(DetalleVentaRequest detalle) {
        Inventario inventario = inventarioRepository
                .findByAlmacenIdAndProductoId(detalle.getAlmacenId(), detalle.getProductoId())
                .orElse(null);

        int disponible = inventario == null ? 0 : inventario.getCantidadActual();
        if (inventario == null || detalle.getCantidad() == null || detalle.getCantidad() > disponible) {
            throw new BusinessRuleException(
                    "Stock insuficiente en inventario para el producto " + detalle.getProductoId()
                            + " en el almacen " + detalle.getAlmacenId()
                            + ". Disponible: " + disponible
                            + " Solicitado: " + detalle.getCantidad());
        }

        inventario.setCantidadActual(disponible - detalle.getCantidad());
        inventario.setFechaActualizacion(LocalDateTime.now());
        inventarioRepository.save(inventario);
    }

    @Override
    public byte[] generateReport(Integer id) {
        Venta ventaRetrieved = ventasRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta", id));
        List<DetalleVenta> detalle = detalleVentaRepository.findByVenta_Id(id);
        Map<String, Object> ventaReportData = new HashMap<>();
        ventaReportData.put("venta", ventaRetrieved);
        ventaReportData.put("detalleVenta", detalle);
        ventaReportData.put("total", detalle.stream()
                .map(d -> d.getPrecioUnitarioVenta().multiply(BigDecimal.valueOf(d.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        return pdfService.generatePdfReport("ventas-report", ventaReportData);
    }
}
