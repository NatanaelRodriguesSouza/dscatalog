package com.example.DsCatalog.Dto;

import com.example.DsCatalog.services.validation.UserInsertValid;

@UserInsertValid
public class UserInsertDTO extends UserDTO{
    private String password;
    public UserInsertDTO(){
        super();
    }
    public String getPassword() {
        return password;
    }
}
