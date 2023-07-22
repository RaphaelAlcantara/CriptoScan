package com.ifpe.criptoscan;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ifpe.criptoscan.api.CriptoOneData;
import com.ifpe.criptoscan.api.CryptoDataListener;
import com.ifpe.criptoscan.model.Alerta;
import com.ifpe.criptoscan.model.CriptoMoeda;
import com.ifpe.criptoscan.model.Notificacao;
import com.ifpe.criptoscan.model.User;

import java.util.List;

public class MyAlarmReceiver extends BroadcastReceiver implements CryptoDataListener {


    private User user;
    private Context context;
    @Override
    public void onReceive(Context context, Intent intent){
        // Inicie o serviço em primeiro plano novamente para criar o próximo alarme
        this.context = context;
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = firebaseAuth.getCurrentUser();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        if(mUser!=null) {
            mDatabase.child(mUser.getUid()).get()
                    .addOnCompleteListener(
                            task1 -> {
                                String msg;
                                if (task1.isSuccessful()) {
                                    user = task1.getResult().getValue(User.class);
                                    for (Alerta a : user.getAlertas()) {
                                        if (a.isAtivo()) {
                                            CriptoOneData criptoOneData = new CriptoOneData(context, a);
                                            criptoOneData.setCryptoDataListener(this);
                                            criptoOneData.fetchCryptoData();
                                        }
                                    }
                                }
                            });
            Intent serviceIntent = new Intent(context, AlarmesService.class);
            ContextCompat.startForegroundService(context, serviceIntent);
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

    }

    @Override
    public void onCryptoOneSearch(Notificacao notificacao) {
        if(notificacao!=null)
        {
            int index = user.getAlertas().indexOf(notificacao.getAlerta());
            user.getAlertas().set(index, notificacao.getAlerta());
            user.getNotificacoes().add(notificacao);
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser mUser = firebaseAuth.getCurrentUser();
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
            mDatabase.child(mUser.getUid()).setValue(user)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Intent newIntent = new Intent(context, CoinDetails.class);
                            newIntent.putExtra("classe", notificacao.getAlerta().getCoin());
                            newIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            PendingIntent pendingNotificationIntent = PendingIntent.getActivity(
                                    context, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            String channelId = createChannel(context);
                            NotificationCompat.Builder builder =
                                    new NotificationCompat.Builder(context, channelId);
                            builder.setSmallIcon(R.drawable.logo);
                            builder.setContentTitle("Obaa, seu alerta foi atingido!");
                            builder.setContentText("")
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(notificacao.getMensagem()+"\nMoeda: "+notificacao.getAlerta().getCoin().getName()));
                            builder.setContentIntent(pendingNotificationIntent);
                            builder.setAutoCancel(true);
                            Notification notification = builder.build();
                            // Pegar o gerenciador de notificações do sistema e exibe a notificação
                            NotificationManager notificationManager = (NotificationManager)
                                    context.getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.notify(0, notification);
                        }
                    });
        }

    }
    private String createChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "CriptoScan_2";
            NotificationChannel channel = new NotificationChannel(
                    channelId, "CriptoScan",
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            return channelId;
        } else {
            return null;
        }
    }
}

