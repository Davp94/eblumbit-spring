package com.blumbit.eblumbit.common.dto;

import lombok.*;

@Getter 
@Setter 
@Builder 
@AllArgsConstructor 
@NoArgsConstructor 
public class PageableRequest<T> {
    private int pageSize;
    private int pageNumber;
    private String sortField;
    private String sortOrder;
    private T criterials;
    private String filterValue;
}
