package com.blumbit.eblumbit.repository.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.blumbit.eblumbit.dto.productos.ProductoFilterCriteria;
import com.blumbit.eblumbit.entities.Producto;

import jakarta.persistence.criteria.Predicate;

public class ProductoSpecification {

    public static Specification<Producto> createSpecification(String filterValue, ProductoFilterCriteria criteria){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(filterValue != null && filterValue.trim().isEmpty())
            {
                String likeFilterValue = "%"+filterValue.toLowerCase()+"%";
                predicates.add(criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("nombre")), likeFilterValue),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("descripcion")), likeFilterValue),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("marca")), likeFilterValue),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("codigoBarra")), likeFilterValue)
                ));
            }

            if(criteria.getNombre() != null && !criteria.getNombre().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nombre")), 
                "%"+criteria.getNombre().toLowerCase()+"%"));
            }

            if(criteria.getDescripcion() != null && !criteria.getDescripcion().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("descripcion")), 
                "%"+criteria.getDescripcion().toLowerCase()+"%"));
            }
            if(criteria.getMarca() != null && !criteria.getMarca().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("marca")), 
                "%"+criteria.getMarca().toLowerCase()+"%"));
            }
            if(criteria.getCodigoBarra() != null && !criteria.getCodigoBarra().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("codigo_barra")), 
                "%"+criteria.getCodigoBarra().toLowerCase()+"%"));
            }
            if(criteria.getNombreCategoria() != null && !criteria.getNombreCategoria().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.join("categoria").get("nombre")),
                "%"+criteria.getNombreCategoria().toLowerCase()+"%"));
            }
            if(criteria.getAlmacenId() != null && criteria.getAlmacenId() > 0) {
                predicates.add(criteriaBuilder.equal(root.join("inventario").get(("almacen")).get("id"), 
                criteria.getAlmacenId()));
            }

            return criteriaBuilder.and(predicates);

        };
    }

}
