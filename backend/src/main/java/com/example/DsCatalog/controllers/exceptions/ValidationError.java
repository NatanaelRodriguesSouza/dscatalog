package com.example.DsCatalog.controllers.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandarError{
    private List<FieldMessage> erros = new ArrayList<>();

    public List<FieldMessage> getErros() {
        return erros;
    }
    public void addError(String fielName , String message){
        erros.add(new FieldMessage(fielName,message));
    }
}
