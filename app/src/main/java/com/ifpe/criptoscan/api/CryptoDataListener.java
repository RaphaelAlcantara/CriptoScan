package com.ifpe.criptoscan.api;

import com.ifpe.criptoscan.model.CriptoMoeda;
import com.ifpe.criptoscan.model.Notificacao;

import java.util.List;

public interface CryptoDataListener {

    void onCryptoTopDataReceived(List<CriptoMoeda> crypto);
    void onCryptoListDataReceived(List<CriptoMoeda> newCrypto);
    void onCryptoChartDataReceived(List<String[]> cryptoData);
    void onCryptoOneSearch(Notificacao notificacao);
}
