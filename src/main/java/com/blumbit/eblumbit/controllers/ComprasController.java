package com.blumbit.eblumbit.controllers;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blumbit.eblumbit.dto.compras.ComprasRequest;
import com.blumbit.eblumbit.dto.compras.ComprasResponse;
import com.blumbit.eblumbit.services.spec.IComprasService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/compras")
@RequiredArgsConstructor
public class ComprasController {

    private final IComprasService comprasService;

    @GetMapping
    public ResponseEntity<List<ComprasResponse>> findAllCompras() {
        return ResponseEntity.ok(comprasService.findAllCompras());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComprasResponse> findCompraById(@PathVariable Integer id) {
        return ResponseEntity.ok(comprasService.findCompraById(id));
    }

    @GetMapping("{compraId}/report")
    public ResponseEntity<byte[]> getReportCompra(@PathVariable Integer compraId) {
        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_PDF)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment: filename=compras-report.pdf")
            .body(comprasService.generateReport(compraId));
    }
    

    @PostMapping
    public ResponseEntity<ComprasResponse> createCompra(@Valid @RequestBody ComprasRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(comprasService.createCompra(request));
    }
}