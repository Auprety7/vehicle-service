package com.example.vehicles.exception;

import java.util.Map;

public class BadRequestException  extends  RuntimeException{

    public BadRequestException(String message) {
        super("Bad request: " + message);
    }
}
