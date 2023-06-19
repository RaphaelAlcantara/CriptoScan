package com.ifpe.criptoscan.api;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

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
            JSONArray data = response.getJSONArray("Data");

            // Verificar se há pelo menos 5 elementos no array
            if (data.length() >= 5) {
                JSONObject coin1 = data.getJSONObject(0);
                JSONObject coinInfo1 = coin1.getJSONObject("CoinInfo");
                JSONObject display1 = coin1.getJSONObject("DISPLAY");
                String coinName1 = coinInfo1.getString("Name");
                String coinFullName1 = coinInfo1.getString("FullName");
                String price1 = display1.getJSONObject("BRL").getString("PRICE");
                String variant1 = display1.getJSONObject("BRL").getString("CHANGEPCT24HOUR");
                String MKTCAP1 = display1.getJSONObject("BRL").getString("MKTCAP");

                JSONObject coin2 = data.getJSONObject(1);
                JSONObject coinInfo2 = coin2.getJSONObject("CoinInfo");
                JSONObject display2 = coin2.getJSONObject("DISPLAY");
                String coinName2 = coinInfo2.getString("Name");
                String coinFullName2 = coinInfo2.getString("FullName");
                String price2 = display2.getJSONObject("BRL").getString("PRICE");
                String variant2 = display2.getJSONObject("BRL").getString("CHANGEPCT24HOUR");
                String MKTCAP2 = display2.getJSONObject("BRL").getString("MKTCAP");

                JSONObject coin3 = data.getJSONObject(2);
                JSONObject coinInfo3 = coin3.getJSONObject("CoinInfo");
                JSONObject display3 = coin3.getJSONObject("DISPLAY");
                String coinName3 = coinInfo3.getString("Name");
                String coinFullName3 = coinInfo3.getString("FullName");
                String price3 = display3.getJSONObject("BRL").getString("PRICE");
                String variant3 = display3.getJSONObject("BRL").getString("CHANGEPCT24HOUR");
                String MKTCAP3 = display3.getJSONObject("BRL").getString("MKTCAP");

                JSONObject coin4 = data.getJSONObject(3);
                JSONObject coinInfo4 = coin4.getJSONObject("CoinInfo");
                JSONObject display4 = coin4.getJSONObject("DISPLAY");
                String coinName4 = coinInfo4.getString("Name");
                String coinFullName4 = coinInfo4.getString("FullName");
                String price4 = display4.getJSONObject("BRL").getString("PRICE");
                String variant4 = display4.getJSONObject("BRL").getString("CHANGEPCT24HOUR");
                String MKTCAP4 = display4.getJSONObject("BRL").getString("MKTCAP");

                JSONObject coin5 = data.getJSONObject(4);
                JSONObject coinInfo5 = coin5.getJSONObject("CoinInfo");
                JSONObject display5 = coin5.getJSONObject("DISPLAY");
                String coinName5 = coinInfo5.getString("Name");
                String coinFullName5 = coinInfo5.getString("FullName");
                String price5 = display5.getJSONObject("BRL").getString("PRICE");
                String variant5 = display5.getJSONObject("BRL").getString("CHANGEPCT24HOUR");
                String MKTCAP5 = display5.getJSONObject("BRL").getString("MKTCAP");


                // Atribuir os valores aos restantes das variáveis (coin3, coin4, coin5)

                // Chamar o método onCryptoDataReceived do ouvinte
                if (cryptoDataListener != null) {
                    cryptoDataListener.onCryptoDataReceived(
                            coinName1,
                            coinFullName1,
                            price1,
                            variant1,
                            MKTCAP1,

                            coinName2,
                            coinFullName2,
                            price2,
                            variant2,
                            MKTCAP2,

                            coinName3,
                            coinFullName3,
                            price3,
                            variant3,
                            MKTCAP3,

                            coinName4,
                            coinFullName4,
                            price4,
                            variant4,
                            MKTCAP4,

                            coinName5,
                            coinFullName5,
                            price5,
                            variant5,
                            MKTCAP5);

                    // Chamar o método onCryptoDataReceived para os restantes das variáveis
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void setCryptoDataListener(CryptoDataListener listener) {
        this.cryptoDataListener = listener;
    }





}
