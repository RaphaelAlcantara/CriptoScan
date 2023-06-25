package com.ifpe.criptoscan.api;

import android.graphics.Color;

import com.ifpe.criptoscan.model.CriptoMoeda;

import java.util.List;

public interface CryptoDataListener {

    void onCryptoDataReceived(List<CriptoMoeda> crypto);



}
