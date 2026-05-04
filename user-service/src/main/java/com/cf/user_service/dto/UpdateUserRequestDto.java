package com.cf.user_service.dto;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

public class UpdateUserRequestDto {
    @NotBlank(message = "Name can't be empty")
    @Size(min = 2, max = 50, message = "Length of name must be between 2 and 50")
    private String name;

    @NotBlank(message = "Surname can't be empty")
    @Size(min = 2, max = 50, message = "Length of surname must be between 2 and 50")
    private String surname;

    @NotBlank(message = "Email can't be empty")
    @Email(message = "Email must be valid")
    private String email;

     public UpdateUserRequestDto(String name, String surname, String email) {
         this.name = name;
         this.surname = surname;
         this.email = email;
     }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }
}
