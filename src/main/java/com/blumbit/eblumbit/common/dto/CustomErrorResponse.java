package com.blumbit.eblumbit.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Envelope de error compatible con ProblemDetails de ASP.NET Core (.NET),
 * que es el contrato canonico del proyecto.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomErrorResponse {

    private String type;

    private String title;

    private int status;

    private String detail;

    private String instance;

    private String traceId;
}
