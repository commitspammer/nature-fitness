package com.naturefitness.springrestapi.exception;

public class NonUniqueFieldException extends RuntimeException {

    public NonUniqueFieldException(String message) {
        super(message);
    }

}
