package com.blumbit.eblumbit.validation.annotation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.blumbit.eblumbit.validation.spec.IUniqueNameChecker;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component 
public class UniqueNameValidator implements ConstraintValidator<UniqueName, String>{

    @Autowired 
    private IUniqueNameChecker uniqueNameChecker;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null | value.isBlank()){
            return true;
        }

        boolean isUnique = uniqueNameChecker.isUniqueName(value);
        if(!isUnique) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("El valor del username ya existe")
            .addConstraintViolation();
            return false;
        }

        return true;
    }
}
