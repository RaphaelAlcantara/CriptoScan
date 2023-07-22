package com.ifpe.criptoscan.api;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ifpe.criptoscan.model.Alerta;
import com.ifpe.criptoscan.model.CriptoMoeda;
import com.ifpe.criptoscan.model.Notificacao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CriptoOneData {
    private Context context;

    private CryptoDataListener cryptoDataListener;
    private Alerta alerta;



    public CriptoOneData(Context context, Alerta alerta) {
        this.context = context;
        this.alerta = alerta;
    }
    public void fetchCryptoData() {
        String url = "https://min-api.cryptocompare.com/data/price?fsym="+alerta.getCoin().getName()+
                "&tsyms=BRL&api_key=dc2f04ccfc6ba0e5846f74975068aef352459275defb331371e7e184459796fa";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        processCryptoData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Erro ao obter dados da API", Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(context).add(request);
    }

    public void processCryptoData(JSONObject response) {
        try {
            Notificacao notificacao=null;
            double preco = Double.parseDouble(response.getString("BRL"));
            /*System.out.println("Moeda: "+alerta.getCoin().getName()+
                    "\nValor do alerta: "+alerta.getValor()+
                    "\nValor Recebido da leitura: "+preco);*/
            if(alerta.isDownPrices())
            {
                if(preco<alerta.getValor())
                {
                    alerta.setAtivo(false);
                    notificacao=new Notificacao(alerta,"O Valor desceu como você colocou, aproveite!");
                }
            }
            else{
                if(preco>alerta.getValor())
                {
                    alerta.setAtivo(false);
                    notificacao=new Notificacao(alerta,"O Valor subiu como você colocou, aproveite!");
                }
            }
            if (cryptoDataListener != null) {
                cryptoDataListener.onCryptoOneSearch(notificacao);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void setCryptoDataListener(CryptoDataListener listener) {
        this.cryptoDataListener = listener;
    }
}
