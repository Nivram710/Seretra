package de.nivram710.seretra;

import android.annotation.SuppressLint;
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
import android.os.Vibrator;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import de.nivram710.seretra.notificationServices.pauseLocationListenerService;
import de.nivram710.seretra.notificationServices.startLocationListenerService;
import de.nivram710.seretra.notificationServices.stopLocationListenerService;

/**
 * Die Klasse ist für die Ortung des Nutzers verantwortlich
 */
public class gps_service extends Service {

    private static String upload_url_String = "http://nivram710.ddns.net/upload.php";
    private static LocationManager locationManager;
    private static LocationListener locationListener;
    private static String status_channel_id = "StatusChannel";
    private static boolean pause = false;
    private static boolean gpsEnabled = true;
    private static int minTimeGPS = 1000;
    private static int minDistanceGPS = 5;
    private static int vibrationDurration = 2000;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onCreate() {
        super.onCreate();
        showNotification();
    }

    /**
     * In der onStartCommand Methode wird ein Location Listener erstellt
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                Intent intent = new Intent("location_update");
                intent.putExtra("location", location.getLatitude() + ";" + location.getLongitude() + ";" + location.getAltitude());

                new Thread() {
                    /**
                     * Dies Methode sendet die GPS-Daten zur Datenbank
                     */
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @SuppressLint({"HardwareIds", "MissingPermission"})
                    public void run() {
                        String imei;

                        String longtitude = String.valueOf(location.getLongitude());
                        String latitude = String.valueOf(location.getLatitude());
                        String altitude = String.valueOf(location.getAltitude());



                        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                        if (telephonyManager != null) {
                            imei = telephonyManager.getImei();
                        } else {
                            imei = "0";
                        }

                        String data;
                        data = "?imei=" + imei;
                        data += "&longtitude=" + longtitude;
                        data += "&latitude=" + latitude;
                        data += "&altitude=" + altitude;

                        try {
                            InputStream response = new URL(upload_url_String + data).openStream();
                            String responseBody = new Scanner(response).useDelimiter("\\A").next();

                            if(!responseBody.equals("-1")) {
                                Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(VIBRATOR_SERVICE);
                                if(vibrator != null) {
                                    vibrator.vibrate(vibrationDurration);
                                }

                            }
                            response.close();
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
                gpsEnabled = true;
                showNotification();
            }

            /**
             * Die Methode überprüft, ob GPS aktiv ist, ansonsten werden die Einstellungen
             * im Unterpunk GPS geöffnet, damit GPS wieder aktiviert wird
             */
            @Override
            public void onProviderDisabled(String s) {
                gpsEnabled = false;
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                Toast.makeText(gps_service.this, getText(R.string.toast_settings_location), Toast.LENGTH_LONG).show();
                showNotification();
            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTimeGPS, minDistanceGPS, locationListener);

        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * Diese Methode startet den Foreground Service
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel statusChannel = new NotificationChannel(status_channel_id, getText(R.string.notification_channel_name), NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(statusChannel);
            }
        }

        if(gpsEnabled) {
            startForeground(getForegroundID(), createNotification(getText(R.string.app_name), getText(R.string.notification_text_start), R.drawable.notification_icon, Color.rgb(0, 125, 160)));
        } else {
            startForeground(getForegroundID(), createNotification(getText(R.string.app_name), getText(R.string.notification_text_gps_disabled), R.drawable.notification_icon, Color.argb(0, 255, 75, 75)));
        }
    }

    /**
     * Diese Methode erstellt und formatiert die Notification
     */
    private android.app.Notification createNotification(CharSequence title, CharSequence text, int icon, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent startLocationListenerIntent = new Intent(getApplicationContext(), startLocationListenerService.class);
            PendingIntent startLocationListenerPendingIntent = PendingIntent.getService(this, 0, startLocationListenerIntent, 0);
            NotificationCompat.Action startAction = new NotificationCompat.Action(R.drawable.notification_start_location_listener, "Start", startLocationListenerPendingIntent);

            Intent stopLocationListenerIntent = new Intent(getApplicationContext(), stopLocationListenerService.class);
            PendingIntent stopLocationListenerPendingIntent = PendingIntent.getService(this, 0, stopLocationListenerIntent, 0);
            NotificationCompat.Action stopAction = new NotificationCompat.Action(R.drawable.notification_stop_location_listener, "Stop", stopLocationListenerPendingIntent);

            Intent pauseLocationListenerIntent = new Intent(getApplicationContext(), pauseLocationListenerService.class);
            PendingIntent pauseLocationListenerPendingIntent = PendingIntent.getService(this, 0, pauseLocationListenerIntent, 0);
            NotificationCompat.Action pauseAction = new NotificationCompat.Action(R.drawable.notification_pause_location_listener, "Pause", pauseLocationListenerPendingIntent);
            if (pause) {
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, status_channel_id)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setSmallIcon(icon)
                        .setColorized(true)
                        .setColor(color)
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
                        .setColor(color)
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

    /**
     * Die Methode deaktiviert den LocationListener
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) locationManager.removeUpdates(locationListener);
    }

    /**
     * Die Methode gibt den LocationListener zurück
     */
    public static LocationListener getLocationListener() {
        return locationListener;
    }

    /**
     * Die Methode gibt den LocationManager zurück
     */
    public static LocationManager getLocationManager() {
        return locationManager;
    }

    /**
     * Die Methode setzt den Pause-Wert
     */
    public static void setPause(boolean value) {
        pause = value;
    }

    /**
     * Die Methode gibt den Pause-Wert zurück
     */
    public static boolean getPause() {
        return pause;
    }

    /**
     * Die Methode gibt den Zeit-Wert zurück, der vergehen muss,
     * bevor nach der neuen Position gefragt wird
     */
    public static int getMinTimeGPS(){
        return minTimeGPS;
    }

    /**
     * Die Methode gibt den Distanz-Wert zurück, der zurückgelegt werden muss,
     * bevor die neue Position zum Server gesendet wird
     */
    public static int getMinDistanceGPS() {
        return minDistanceGPS;
    }

    /**
     * Die Methode gibt den zurück, ob das GPS-Modul aktiv ist
     */
    public static boolean getGPSEanbled() {
        return gpsEnabled;
    }

    /**
     * Die Methode gibt die Foreground ID zurück
     */
    public static int getForegroundID() {
        return 101;
    }

    /**
     * Die Methode gibt die status_channel_id zurück
     */
    public static String getStatus_channel_id() {
        return status_channel_id;
    }
}