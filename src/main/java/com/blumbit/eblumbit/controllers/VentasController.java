package com.blumbit.eblumbit.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.blumbit.eblumbit.dto.ventas.AnularVentaDto;
import com.blumbit.eblumbit.dto.ventas.VentasRequest;
import com.blumbit.eblumbit.dto.ventas.VentasResponse;
import com.blumbit.eblumbit.services.spec.IVentasService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController 
@RequestMapping("/ventas")
public class VentasController {

    private final IVentasService ventasService;

    public VentasController(IVentasService ventasService) {
        this.ventasService = ventasService;
    }

    @GetMapping
    public ResponseEntity<List<VentasResponse>> findAllVentas() {
        return ResponseEntity.ok(ventasService.findAllVentas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VentasResponse> findVentaById(@PathVariable Integer id) {
        return ResponseEntity.ok(ventasService.findVentaById(id));
    }

    @PostMapping
    public ResponseEntity<VentasResponse> createVenta(@RequestBody VentasRequest ventasRequest) {
        
        return ResponseEntity.created(null).body(ventasService.createVenta(ventasRequest));
    }

    @PatchMapping("/{id}") 
    public ResponseEntity<Void> anularVenta(@PathVariable Integer id, AnularVentaDto anularVentaDto) {
        
        ventasService.anularVenta(id, anularVentaDto);
        return ResponseEntity.noContent().build();
    }
    
    
    

    

}
