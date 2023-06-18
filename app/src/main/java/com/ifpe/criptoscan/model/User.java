package com.ifpe.criptoscan.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public class User {
    private String name;
    private String email;
    private List<CriptoMoeda> favoritos;
    private List<Alerta> alertas;

    public User() {
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
