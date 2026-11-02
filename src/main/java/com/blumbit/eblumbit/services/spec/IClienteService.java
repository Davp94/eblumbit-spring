package com.blumbit.eblumbit.services.spec;

import java.util.List;

import com.blumbit.eblumbit.dto.ClienteDto;
import com.blumbit.eblumbit.dto.CreateClienteDto;

public interface IClienteService {

    List<ClienteDto> findAllClientes();

    ClienteDto findClienteById(Integer id);

    ClienteDto createCliente(CreateClienteDto dto);

    ClienteDto updateCliente(Integer id, CreateClienteDto dto);

    void deleteCliente(Integer id);
}
