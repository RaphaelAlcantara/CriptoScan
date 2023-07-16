package com.ifpe.criptoscan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.ifpe.criptoscan.api.ChartCripto;
import com.ifpe.criptoscan.api.CryptoData;
import com.ifpe.criptoscan.api.CryptoDataListener;
import com.ifpe.criptoscan.model.CriptoMoeda;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class CoinDetails extends AppCompatActivity implements CryptoDataListener {

    private CriptoMoeda moeda;
    private LineChart chart;
    private String resposta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_details);
        Intent intent = getIntent();
        resposta = intent.getStringExtra("moeda");
        TextView t = findViewById(R.id.teste);
        t.setText(resposta);
        chart = findViewById(R.id.lineChart);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        ChartCripto cryptoData = new ChartCripto(getApplicationContext());
        cryptoData.setCryptoDataListener(this);
        cryptoData.fetchCryptoData(resposta);

        //setData(100, 50);
    }

    @Override
    public void onCryptoTopDataReceived(List<CriptoMoeda> crypto) {

    }

    @Override
    public void onCryptoListDataReceived(List<CriptoMoeda> newCrypto) {

    }

    @Override
    public void onCryptoChartDataReceived(List<String[]> cryptoData) {
        List<Entry> entries = new ArrayList<>();
        String dateTime[] = new String[cryptoData.size()];
        int i=0;
        for(String[] response : cryptoData)
        {
            Date date = new Date(Long.parseLong(response[0])*1000L);
            // format of the date
            SimpleDateFormat jdf = new SimpleDateFormat("HH:mm");
            jdf.setTimeZone(TimeZone.getTimeZone("GMT-3"));
            String java_date = jdf.format(date);
            //java_date.replace(" ","\n");
            dateTime[i]=java_date;
            entries.add(new Entry(i,Float.parseFloat(response[1])));
            i++;
        }

        LineDataSet dataSet = new LineDataSet(entries, resposta);
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLACK);

        LineData lineData = new LineData(dataSet);
        chart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return dateTime[(int) value];
            }
        });
        // Configurações do gráfico
        Description description = new Description();
        description.setText("Variações da moeda");
        chart.setDescription(description);
        chart.setData(lineData);
        chart.invalidate(); // Atualiza o gráfico
    }
}