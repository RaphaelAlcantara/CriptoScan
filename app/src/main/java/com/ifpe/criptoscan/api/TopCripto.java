package com.ifpe.criptoscan.api;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ifpe.criptoscan.model.CriptoMoeda;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TopCripto {
    private Context context;

    private CryptoDataListener cryptoDataListener;




    public TopCripto(Context context) {
        this.context = context;
    }

    public void fetchCryptoData() {
        String url = "https://min-api.cryptocompare.com/data/top/totalvolfull?limit=5&tsym=BRL&api_key=dc2f04ccfc6ba0e5846f74975068aef352459275defb331371e7e184459796fa";

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
            List<CriptoMoeda> list = new ArrayList<>();
            JSONArray data = response.getJSONArray("Data");
            for(int i=0;i<data.length();i++) {
                JSONObject coin1 = data.getJSONObject(i);
                JSONObject coinInfo1 = coin1.getJSONObject("CoinInfo");
                JSONObject display1 = coin1.getJSONObject("DISPLAY");
                String coinName1 = coinInfo1.getString("Name");
                String coinFullName1 = coinInfo1.getString("FullName");
                String imageURL = "https://www.cryptocompare.com"+coinInfo1.getString("ImageUrl");
                String price1 = display1.getJSONObject("BRL").getString("PRICE");
                String variant1 = display1.getJSONObject("BRL").getString("CHANGEPCT24HOUR");
                String MKTCAP1 = display1.getJSONObject("BRL").getString("MKTCAP");
                CriptoMoeda cpr = new CriptoMoeda(coinName1,coinFullName1,price1,variant1,MKTCAP1, imageURL);
                list.add(cpr);
            }
            if (cryptoDataListener != null) {
                    cryptoDataListener.onCryptoTopDataReceived(list);
             }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void setCryptoDataListener(CryptoDataListener listener) {
        this.cryptoDataListener = listener;
    }






}
