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
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
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
        ChartCripto cryptoData = new ChartCripto(getApplicationContext());
        cryptoData.setCryptoDataListener(this);
        cryptoData.fetchCryptoData(resposta);

        //setData(100, 50);
    }
    private void setData(int count, float range) {

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < count; i++) {

            float val = (float) (Math.random() * range) - 30;
            values.add(new Entry(i, val));
        }

        LineDataSet set1;

        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.notifyDataSetChanged();
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "Saida");

            set1.setDrawIcons(false);

            // draw dashed line
            set1.enableDashedLine(10f, 5f, 0f);

            // line thickness and point size
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);

            // draw points as solid circles
            set1.setDrawCircleHole(false);

            // customize legend entry
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            // text size of values
            set1.setValueTextSize(9f);

            // draw selection line as dashed
            set1.enableDashedHighlightLine(10f, 5f, 0f);

            // set the filled area
            set1.setDrawFilled(true);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return chart.getAxisLeft().getAxisMinimum();
                }
            });

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the data sets

            // create a data object with the data sets
            LineData data = new LineData(dataSets);

            // set data
            chart.setData(data);
        }
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
        int i=0;
        for(String[] response : cryptoData)
        {
            Date date = new Date(Long.parseLong(response[0])*1000L);
            // format of the date
            SimpleDateFormat jdf = new SimpleDateFormat("HH.mm");
            jdf.setTimeZone(TimeZone.getTimeZone("GMT-3"));
            String java_date = jdf.format(date);
            entries.add(new Entry(i,Float.parseFloat(response[1])));
            i++;
        }

        LineDataSet dataSet = new LineDataSet(entries, resposta);
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLACK);

        LineData lineData = new LineData(dataSet);

        // Configurações do gráfico
        Description description = new Description();
        description.setText("Variações da moeda");
        chart.setDescription(description);
        chart.setData(lineData);
        chart.invalidate(); // Atualiza o gráfico
    }
}