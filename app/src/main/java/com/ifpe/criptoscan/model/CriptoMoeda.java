package com.ifpe.criptoscan.model;

import java.io.Serializable;

public class CriptoMoeda implements Serializable {
    private String name;
    private String fullName;
    private String price;
    private String variant;
    private String MKTCAP;
    private String imageURL;

    public CriptoMoeda(String name, String fullName, String price,
                       String variant, String MKTCAP, String imageURL){
        this.name = name;
        this.fullName = fullName;
        this.price = price;
        this.variant = variant;
        this.MKTCAP = MKTCAP;
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getMKTCAP() {
        return MKTCAP;
    }

    public void setMKTCAP(String MKTCAP) {
        this.MKTCAP = MKTCAP;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
