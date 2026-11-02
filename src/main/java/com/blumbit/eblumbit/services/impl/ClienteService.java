package com.blumbit.eblumbit.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.blumbit.eblumbit.dto.ClienteDto;
import com.blumbit.eblumbit.dto.CreateClienteDto;
import com.blumbit.eblumbit.entities.Cliente;
import com.blumbit.eblumbit.exception.ResourceNotFoundException;
import com.blumbit.eblumbit.repository.ClienteRepository;
import com.blumbit.eblumbit.services.spec.IClienteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteService implements IClienteService {

    private final ClienteRepository clienteRepository;

    @Override
    public List<ClienteDto> findAllClientes() {
        return clienteRepository.findAll().stream()
                .map(ClienteDto::fromEntityData)
                .toList();
    }

    @Override
    public ClienteDto findClienteById(Integer id) {
        return ClienteDto.fromEntityData(findEntity(id));
    }

    @Override
    @Transactional
    public ClienteDto createCliente(CreateClienteDto dto) {
        return ClienteDto.fromEntityData(clienteRepository.save(dto.toEntity()));
    }

    @Override
    @Transactional
    public ClienteDto updateCliente(Integer id, CreateClienteDto dto) {
        Cliente cliente = findEntity(id);
        cliente.setNombreCompleto(dto.getNombreCompleto());
        cliente.setNroIdentificacion(dto.getNroIdentificacion());
        cliente.setFechaNacimiento(dto.resolveFechaNacimiento());
        cliente.setTelefono(dto.getTelefono());
        cliente.setCorreo(dto.getCorreo());
        return ClienteDto.fromEntityData(clienteRepository.save(cliente));
    }

    @Override
    @Transactional
    public void deleteCliente(Integer id) {
        clienteRepository.delete(findEntity(id));
    }

    private Cliente findEntity(Integer id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente", id));
    }
}
