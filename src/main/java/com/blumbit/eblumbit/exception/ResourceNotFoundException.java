package com.blumbit.eblumbit.exception;

public class ResourceNotFoundException extends DomainException{

    public ResourceNotFoundException(String resource, Object id) {
        super(resource + " con id: "+id+ " no encontrado", 404, "RESOURCE_NOT_FOUND");
    }

}
