package com.ifpe.criptoscan.model;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class Alerta implements Serializable {
    private CriptoMoeda coin;
    private double valor;
    private boolean downPrices;//false está subindo, true. .descendo;
    private boolean ativo;
    public Alerta() {
    }

    public Alerta(CriptoMoeda coin, double valor, boolean downPrices) {
        this.coin = coin;
        this.valor = valor;
        this.downPrices = downPrices;
    }

    public CriptoMoeda getCoin() {
        return coin;
    }

    public void setCoin(CriptoMoeda coin) {
        this.coin = coin;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public boolean isDownPrices() {
        return downPrices;
    }

    public void setDownPrices(boolean downPrices) {
        this.downPrices = downPrices;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Alerta aux = (Alerta) obj;
        if(aux.getValor() == this.getValor() && aux.getCoin().getName()==this.getCoin().getName())
        {
            return true;
        }
        return false;
    }
}
