package com.ifpe.criptoscan.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class User implements Serializable {
    private String name;
    private String email;
    private List<CriptoMoeda> favoritos;
    private List<Alerta> alertas;

    public User() {
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.favoritos = new ArrayList<>();
        this.alertas = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<CriptoMoeda> getFavoritos() {
        return favoritos;
    }

    public void setFavoritos(List<CriptoMoeda> favoritos) {
        this.favoritos = favoritos;
    }

    public List<Alerta> getAlertas() {
        return alertas;
    }

    public void setAlertas(List<Alerta> alertas) {
        this.alertas = alertas;
    }
}
