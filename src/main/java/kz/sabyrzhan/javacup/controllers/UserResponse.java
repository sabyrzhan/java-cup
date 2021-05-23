package kz.sabyrzhan.javacup.controllers;

import lombok.Data;

@Data
public class UserResponse {
    private int code;
    private String name;
    private String phone;
}
