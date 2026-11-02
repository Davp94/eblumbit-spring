package com.blumbit.eblumbit.controllers;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blumbit.eblumbit.dto.ventas.VentasDetailResponse;
import com.blumbit.eblumbit.dto.ventas.VentasRequest;
import com.blumbit.eblumbit.dto.ventas.VentasResponse;
import com.blumbit.eblumbit.services.spec.IVentasService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping({ "/api/ventas", "/ventas" })
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('admin:ventas')")
public class VentasController {

    private final IVentasService ventasService;

    @GetMapping
    public ResponseEntity<List<VentasResponse>> findAllVentas() {
        return ResponseEntity.ok(ventasService.findAllVentas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentasDetailResponse> findVentaById(@PathVariable Integer id) {
        return ResponseEntity.ok(ventasService.findVentaById(id));
    }

    @GetMapping("/{ventaId}/report")
    public ResponseEntity<byte[]> getReportVenta(@PathVariable Integer ventaId) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"venta_" + ventaId + "_report.pdf\"")
                .body(ventasService.generateReport(ventaId));
    }

    @PostMapping
    public ResponseEntity<VentasResponse> createVenta(@RequestBody VentasRequest ventasRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ventasService.createVenta(ventasRequest));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> anularVenta(@PathVariable Integer id) {
        ventasService.anularVenta(id);
        return ResponseEntity.noContent().build();
    }
}
