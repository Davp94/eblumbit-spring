package com.blumbit.eblumbit.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.blumbit.eblumbit.dto.CreateSucursalDto;
import com.blumbit.eblumbit.dto.CreateUsuarioDto;
import com.blumbit.eblumbit.dto.SucursalDto;
import com.blumbit.eblumbit.dto.UsuarioDto;
import com.blumbit.eblumbit.services.spec.ISucursalService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController 
@RequestMapping("/sucursales")
public class SucursalController {

    @Autowired 
    private ISucursalService sucursalService;

    @GetMapping
    public ResponseEntity<List<SucursalDto>> getAllSucursales() {
        return ResponseEntity.ok(sucursalService.findAllSucursales());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SucursalDto> getSucursalById(@PathVariable Integer id) {
        return ResponseEntity.ok(sucursalService.findSucursalById(id));        
    }

    @PostMapping
    public ResponseEntity<SucursalDto> createSucursal(@RequestBody CreateSucursalDto createUsuarioDto) {
        //TODO: process POST request
        SucursalDto sucursalSaved = sucursalService.createSucursal(createUsuarioDto);
        URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(sucursalSaved.getId())
                    .toUri();
        return ResponseEntity.created(location).body(sucursalSaved);    
    }

    @PutMapping("/{id}")
    public ResponseEntity<SucursalDto> updateSucursal(@PathVariable Integer id, @RequestBody CreateSucursalDto createUsuarioDto) {
        
        return ResponseEntity.ok(sucursalService.updateSucursal(id,createUsuarioDto));
    }

    @DeleteMapping("/{id}") 
    public ResponseEntity<Void> deleteSucursal(@PathVariable Integer id, @RequestBody CreateSucursalDto createUsuarioDto) {
        sucursalService.deleteSucursal(id);
        return ResponseEntity.noContent().build();
    }
    
    
    
}
