package com.example.DsCatalog.services.excepetions;

public class DatabaseException extends ResourceNotFoundException{
    public DatabaseException(String msg) {
        super(msg);
    }
}
