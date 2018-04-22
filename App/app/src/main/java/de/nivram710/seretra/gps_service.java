package de.nivram710.seretra;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import de.nivram710.seretra.notificationServices.pauseLocationListenerService;
import de.nivram710.seretra.notificationServices.startLocationListenerService;
import de.nivram710.seretra.notificationServices.stopLocationListenerService;

/**
 * Die Klasse ist für die Ortung des Nutzers verantwortlich
 */
public class gps_service extends Service {

    private static LocationManager locationManager;
    private static LocationListener locationListener;
    private String status_channel_id = "StatusChannel";
    private static boolean pause = false;
    private static int foregroundID = 101;
    private static int minTimeGPS = 1000;
    private static int minDistanceGPS = 5;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onCreate() {
        super.onCreate();
        showNotification();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                Intent intent = new Intent("location_update");
                intent.putExtra("location", location.getLatitude() + ";" + location.getLongitude());

                new Thread() {
                    /**
                     * Dies Methode sendet die GPS-Daten zur Datenbank
                     */
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @SuppressLint({"HardwareIds", "MissingPermission"})
                    public void run() {
                        String getData;
                        String imei;

                        String longtitude = String.valueOf(location.getLongitude());
                        String latitude = String.valueOf(location.getLatitude());


                        String upload_url_String = "http://nivram710.ddns.net/upload.php";

                        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                        if (telephonyManager != null) {
                            imei = telephonyManager.getImei();
                        } else {
                            imei = "0";
                        }

                        getData = "?imei=";
                        getData += imei;
                        getData += "&longtitude=";
                        getData += longtitude;
                        getData += "&latitude=";
                        getData += latitude;

                        try {

                            URL upload_url = new URL(upload_url_String + getData);

                            HttpURLConnection httpURLConnectionUpload = (HttpURLConnection) upload_url.openConnection();
                            httpURLConnectionUpload.setDoOutput(true);
                            httpURLConnectionUpload.setDoInput(true);
                            InputStream inputStream = httpURLConnectionUpload.getInputStream();
                            inputStream.close();
                            httpURLConnectionUpload.disconnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                sendBroadcast(intent);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {
            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Toast.makeText(gps_service.this, getText(R.string.toast_settings_location), Toast.LENGTH_LONG).show();
            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTimeGPS, minDistanceGPS, locationListener);

        return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotification() {
        int status_id = 101;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel statusChannel = new NotificationChannel(status_channel_id, getText(R.string.notification_channel_name), NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(statusChannel);
            }
        }

        startForeground(status_id, createNotification(getText(R.string.app_name), getText(R.string.notification_text), R.drawable.common_google_signin_btn_icon_dark));
    }

    private Notification createNotification(CharSequence title, CharSequence text, int icon) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Intent startLocationListenerIntent = new Intent(getApplicationContext(), startLocationListenerService.class);
                PendingIntent startLocationListenerPendingIntent = PendingIntent.getService(this, 0, startLocationListenerIntent, 0);
                NotificationCompat.Action startAction = new NotificationCompat.Action(R.drawable.notification_pause_location_listener, "Start", startLocationListenerPendingIntent);

                Intent stopLocationListenerIntent = new Intent(getApplicationContext(), stopLocationListenerService.class);
                PendingIntent stopLocationListenerPendingIntent = PendingIntent.getService(this, 0, stopLocationListenerIntent, 0);
                NotificationCompat.Action stopAction = new NotificationCompat.Action(R.drawable.notification_pause_location_listener, "Stop", stopLocationListenerPendingIntent);

                Intent pauseLocationListenerIntent = new Intent(getApplicationContext(), pauseLocationListenerService.class);
                PendingIntent pauseLocationListenerPendingIntent = PendingIntent.getService(this, 0, pauseLocationListenerIntent, 0);
                NotificationCompat.Action pauseAction = new NotificationCompat.Action(R.drawable.notification_pause_location_listener, "Pause", pauseLocationListenerPendingIntent);
                if (pause) {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) locationManager.removeUpdates(locationListener);
    }

    public static LocationManager getLocationManager() {
        return locationManager;
    }

    public static LocationListener getLocationListener() {
        return locationListener;
    }

    public static void setPause(boolean value) {
        pause = value;
    }

    public static boolean getPause() {
        return pause;
    }

    public static int getMinTimeGPS(){
        return minTimeGPS;
    }

    public static int getMinDistanceGPS() {
        return minDistanceGPS;
    }

    public static int getForegroundID() {
        return foregroundID;
    }
}