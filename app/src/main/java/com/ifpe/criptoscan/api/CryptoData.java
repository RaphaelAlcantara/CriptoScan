package com.ifpe.criptoscan.api;

public class CryptoData {
    private String coinName;
    private String coinFullName;
    private String price;
    private String lastUpdate;

    public CryptoData(String coinName, String coinFullName, String price, String lastUpdate) {
        this.coinName = coinName;
        this.coinFullName = coinFullName;
        this.price = price;
        this.lastUpdate = lastUpdate;
    }

    public String getCoinName() {
        return coinName;
    }

    public String getCoinFullName() {
        return coinFullName;
    }

    public String getPrice() {
        return price;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }
}

