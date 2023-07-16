package com.ifpe.criptoscan.model;

import java.io.Serializable;

public class Alerta implements Serializable {
    private CriptoMoeda coin;
    private double percent;
    private boolean downPrices;//false está subindo, true. .descendo;

    public Alerta() {
    }

    public Alerta(CriptoMoeda coin, double percent, boolean downPrices) {
        this.coin = coin;
        this.percent = percent;
        this.downPrices = downPrices;
    }

    public CriptoMoeda getCoin() {
        return coin;
    }

    public void setCoin(CriptoMoeda coin) {
        this.coin = coin;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public boolean isDownPrices() {
        return downPrices;
    }

    public void setDownPrices(boolean downPrices) {
        this.downPrices = downPrices;
    }
}
