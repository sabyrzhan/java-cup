package kz.sabyrzhan.javacup.models;

import lombok.Data;

import java.util.List;

@Data
public class UserPhones {
    public static final UserPhones EMPTY_USER = new UserPhones();

    private List<String> phones;
}
