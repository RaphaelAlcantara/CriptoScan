package com.ifpe.criptoscan.api;

import com.ifpe.criptoscan.model.CriptoMoeda;

import java.util.List;

public interface CryptoDataListener {

    void onCryptoTopDataReceived(List<CriptoMoeda> crypto);
    void onCryptoListDataReceived(List<CriptoMoeda> newCrypto);



}
