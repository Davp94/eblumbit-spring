package com.blumbit.eblumbit.exception;

public class BusinessRuleException  extends DomainException{

    public BusinessRuleException(String message) {
        super(message, 422, "BUSINESS_RULE_EXCEPTION");
    }

}
