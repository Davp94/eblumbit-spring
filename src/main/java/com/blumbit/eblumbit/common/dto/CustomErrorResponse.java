package com.blumbit.eblumbit.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter 
@Setter 
@Builder 
@AllArgsConstructor 
@NoArgsConstructor 
public class CustomErrorResponse<T> {

    private int statusCode;

    private String errorCode;

    private String timestamp;

    private T message;

    private String path;

}
