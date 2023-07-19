package com.ifpe.criptoscan;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ifpe.criptoscan.api.ChartCripto;
import com.ifpe.criptoscan.api.CryptoDataListener;
import com.ifpe.criptoscan.model.Alerta;
import com.ifpe.criptoscan.model.CriptoMoeda;
import com.ifpe.criptoscan.model.User;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class CoinDetails extends AppCompatActivity implements CryptoDataListener {

    private CriptoMoeda moeda;
    private NetworkImageView imageView;
    private LineChart chart;
    private String resposta;
    private User user;
    private RequestQueue queue;
    private ImageLoader loader;
    private Button favorito;
    private Button alerta;
    private int enableFav;
    private int enableAlerta;
    private float ultimoPrecoMoeda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_details);
        enableFav=0;
        enableAlerta=0;
        favorito = findViewById(R.id.button_favoritos);
        alerta = findViewById(R.id.button_alertas);
        this.queue = Volley.newRequestQueue(this);
        Intent intent = getIntent();
        resposta = intent.getStringExtra("moeda");
        moeda = (CriptoMoeda) intent.getSerializableExtra("classe");
        TextView t = findViewById(R.id.teste);
        t.setText(moeda.getName());
        chart = findViewById(R.id.lineChart);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        ChartCripto cryptoData = new ChartCripto(getApplicationContext());
        cryptoData.setCryptoDataListener(this);
        cryptoData.fetchCryptoData(moeda.getName());
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = firebaseAuth.getCurrentUser();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabase.child(mUser.getUid()).get()
                .addOnCompleteListener(
                        task1 -> {
                            String msg;
                            if(task1.isSuccessful())
                            {
                                user = task1.getResult().getValue(User.class);
                                if(user.getFavoritos().contains(moeda))
                                {
                                    favorito.setText("Remover Favorito");
                                    enableFav=1;
                                }
                                else{
                                    favorito.setText("Adicionar Favorito");
                                }
                                favorito.setEnabled(true);
                                alerta.setEnabled(true);
                            }
                        });
        this.loader = new ImageLoader(queue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<>(10);
            public void putBitmap(String url, Bitmap bitmap) { mCache.put(url, bitmap); }
            public Bitmap getBitmap(String url) { return mCache.get(url); }
        });
        imageView = findViewById(R.id.coindetails_image);
        imageView.setImageUrl(moeda.getImageURL(), loader);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void addFavorito(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String msg;
        if(enableFav==0)
        {
            msg="Adicionar aos Favoritos?";
        }
        else {
            msg="Remover dos favoritos?";
        }
        builder.setMessage(msg)
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String msgSaida;
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        DatabaseReference drUsers = FirebaseDatabase.
                                getInstance().getReference("users");
                        if (enableFav == 0) {
                            user.getFavoritos().add(moeda);
                            msgSaida=" - Adicionado aos favoritos";
                            favorito.setText("Remover Favorito");
                            enableFav=1;
                        }
                        else {
                            user.getFavoritos().remove(moeda);
                            msgSaida=" - Removido dos favoritos";
                            favorito.setText("Adicionar Favorito");
                            enableFav=0;
                        }
                        drUsers.child(mAuth.getCurrentUser().getUid()).setValue(user)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(CoinDetails.this, moeda.getName() + msgSaida,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        builder.create().show();
    }

    public void addAlerta(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String msg;
        if(enableAlerta==0)
        {

            msg="Adicionar Alerta? Valor de refência atual: R$ "+ultimoPrecoMoeda;
        }
        else {
            msg="Remover Alerta?";
        }
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_layout, null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(dialogView);
        RadioButton rb =  dialogView.findViewById(R.id.radio_button1);
        EditText number = dialogView.findViewById(R.id.input_number);
        builder.setMessage(msg)

                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Alerta alerta = new Alerta();
                        if(rb.isChecked())
                        {
                            alerta.setDownPrices(false);
                        }
                        else {
                            alerta.setDownPrices(true);
                        }
                        float valor = Float.parseFloat(number.getText().toString());
                        alerta.setCoin(moeda);
                        alerta.setValor(valor);
                        alerta.setAtivo(true);
                        String msgSaida;
                        FirebaseAuth mAuth = FirebaseAuth.getInstance();
                        DatabaseReference drUsers = FirebaseDatabase.
                                getInstance().getReference("users");
                        if (enableAlerta == 0) {
                            user.getAlertas().add(alerta);
                            msgSaida=" - Adicionado aos Alertas";
                        }
                        else {
                            user.getAlertas().remove(alerta);
                            msgSaida=" - Removido dos alertas";
                        }
                        drUsers.child(mAuth.getCurrentUser().getUid()).setValue(user)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(CoinDetails.this, moeda.getName() + msgSaida,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        builder.create().show();
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
            ultimoPrecoMoeda=Float.parseFloat(response[1]);
            entries.add(new Entry(i,Float.parseFloat(response[1])));
            i++;
        }

        LineDataSet dataSet = new LineDataSet(entries, resposta);
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.BLACK);

        LineData lineData = new LineData(dataSet);
        chart.getXAxis().setLabelCount(8);
        chart.getXAxis().setValueFormatter((value, axis) -> dateTime[(int) value]);
        chart.getAxisRight().setEnabled(false);
        // Configurações do gráfico
        Description description = new Description();
        description.setText("Variações da moeda");
        chart.setDescription(description);
        chart.setData(lineData);
        chart.getLineData().setValueTextSize(5f);
        chart.invalidate(); // Atualiza o gráfico
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}