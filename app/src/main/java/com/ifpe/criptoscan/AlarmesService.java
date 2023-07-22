package com.ifpe.criptoscan;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class AlarmesService extends Service {

    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "CriptoScan_1";
    private static final String CHANNEL_NAME = "CriptoScan";

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private static final int ALARM_INTERVAL = 1 * 60 * 1000; // 10 minutos em milissegundos

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Inicie o serviço em primeiro plano com uma notificação
        startForeground(NOTIFICATION_ID, createNotification());

        // Inicie o AlarmManager para disparar o BroadcastReceiver a cada 10 minutos
        setupAlarm();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeAlarm();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private Notification createNotification() {
        // Construir a notificação
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("CriptoScan")
                .setContentText("Serviço em execução...")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        return builder.build();
    }

    private void setupAlarm() {
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyAlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        // Defina o tempo em milissegundos quando o alarme será disparado (por exemplo, daqui a 10 minutos)
        long triggerTime = System.currentTimeMillis() + ALARM_INTERVAL;

        // Agende o alarme para disparar o BroadcastReceiver após o intervalo de 10 minutos
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        }
    }

    private void removeAlarm() {
        // Cancelar o alarme quando o serviço for encerrado
        if (alarmManager != null && pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
}

