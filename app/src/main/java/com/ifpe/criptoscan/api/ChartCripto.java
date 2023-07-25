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

public class ChartCripto {
    private Context context;

    private CryptoDataListener cryptoDataListener;




    public ChartCripto(Context context) {
        this.context = context;
    }

    public void fetchCryptoData(String coinName) {
        String url = "https://min-api.cryptocompare.com/data/v2/histohour?fsym="+coinName+
                "&tsym=BRL&limit=24&api_key=dc2f04ccfc6ba0e5846f74975068aef352459275defb331371e7e184459796fa";
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
            List<String[]> list = new ArrayList<>();
            JSONObject dataObject = response.getJSONObject("Data");
            JSONArray data = dataObject.getJSONArray("Data");
            for(int i=0;i<data.length();i++) {
                String moeda[] = new String[2];
                JSONObject coin1 = data.getJSONObject(i);
                moeda[0] = coin1.getString("time");
                moeda[1] = coin1.getString("open");
                list.add(moeda);
            }
            if (cryptoDataListener != null) {
                cryptoDataListener.onCryptoChartDataReceived(list);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void setCryptoDataListener(CryptoDataListener listener) {
        this.cryptoDataListener = listener;
    }

}
