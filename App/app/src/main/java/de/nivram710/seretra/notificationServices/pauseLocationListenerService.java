package de.nivram710.seretra.notificationServices;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import de.nivram710.seretra.R;
import de.nivram710.seretra.gps_service;

@SuppressLint("Registered")
public class pauseLocationListenerService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Diese Methode zeigt die Notification an
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LocationManager locationManager = gps_service.getLocationManager();
        LocationListener locationListener = gps_service.getLocationListener();
        locationManager.removeUpdates(locationListener);

        gps_service.setPause(true);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            if(gps_service.getGPSEanbled()) {
                startForeground(gps_service.getForegroundID(), createNotification(getText(R.string.app_name), getText(R.string.notification_text_pause), R.drawable.notification_icon));
            } else {
                startForeground(gps_service.getForegroundID(), createNotification(getText(R.string.app_name), getText(R.string.notification_text_gps_disabled), R.drawable.notification_icon));
            }
        } else {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                if(gps_service.getGPSEanbled()) {
                    notificationManager.notify(gps_service.getForegroundID(), createNotification(getText(R.string.app_name), getText(R.string.notification_text_pause), R.drawable.notification_icon));
                } else{
                    notificationManager.notify(gps_service.getForegroundID(), createNotification(getText(R.string.app_name), getText(R.string.notification_text_gps_disabled), R.drawable.notification_icon));
                }
            }
        }
        this.stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Toast.makeText(getApplicationContext(), getText(R.string.toast_warning_pause), Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    /**
     * Diese Methode erstellt und formatiert díe Notification
     */
    private Notification createNotification(CharSequence title, CharSequence text, int icon) {
        String status_channel_id = "StatusChannel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent startLocationListenerIntent = new Intent(getApplicationContext(), startLocationListenerService.class);
            PendingIntent startLocationListenerPendingIntent = PendingIntent.getService(this, 0, startLocationListenerIntent, 0);
            NotificationCompat.Action startAction = new NotificationCompat.Action(R.drawable.notification_start_location_listener, getText(R.string.notification_button_start), startLocationListenerPendingIntent);

            Intent stopLocationListenerIntent = new Intent(getApplicationContext(), stopLocationListenerService.class);
            PendingIntent stopLocationListenerPendingIntent = PendingIntent.getService(this, 0, stopLocationListenerIntent, 0);
            NotificationCompat.Action stopAction = new NotificationCompat.Action(R.drawable.notification_stop_location_listener, getText(R.string.notification_button_stop), stopLocationListenerPendingIntent);

            Intent pauseLocationListenerIntent = new Intent(getApplicationContext(), pauseLocationListenerService.class);
            PendingIntent pauseLocationListenerPendingIntent = PendingIntent.getService(this, 0, pauseLocationListenerIntent, 0);
            NotificationCompat.Action pauseAction = new NotificationCompat.Action(R.drawable.notification_pause_location_listener, getText(R.string.notification_button_pause), pauseLocationListenerPendingIntent);
            if (gps_service.getPause()) {
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, status_channel_id)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setSmallIcon(icon)
                        .setColorized(true)
                        .setColor(Color.argb(0, 0, 125, 160))
                        .addAction(stopAction)
                        .addAction(startAction);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationBuilder.setChannelId(status_channel_id);
                }

                return notificationBuilder.build();
            } else {
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, status_channel_id)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setSmallIcon(icon)
                        .setColorized(true)
                        .setColor(Color.argb(0, 0, 125, 160))
                        .addAction(stopAction)
                        .addAction(pauseAction);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationBuilder.setChannelId(status_channel_id);
                }

                return notificationBuilder.build();
            }
        }
        return null;
    }
}
