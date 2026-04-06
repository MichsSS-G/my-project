package com.cf.user_service.dto;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
public class UserRequestDto {

    @NotBlank(message = "Name can't be empty")
    @Size(min = 2, max = 50, message = "Length of name must be between 2 and 50")
    private String name;

    @NotBlank(message = "Surname can't be empty")
    @Size(min = 2, max = 50, message = "Length of surname must be between 2 and 50")
    private String surname;

    @NotBlank(message = "Email can't be empty")
    @Email(message = "Email should be valid")
    private String email;

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getSurname() {
        return surname;
    }

}
