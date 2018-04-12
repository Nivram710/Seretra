package de.nivram710.seretra;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.support.annotation.RequiresApi;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

/**
 * Die Klasse ist für die Ortung des Nutzers verantwortlich
 */
public class gps_service extends Service {

    String STARTFOREGROUND_ACTION = "com.marothiatechs.foregroundservice.action.startforeground";

    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onCreate() {
        super.onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        showNotification();

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
            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);

        return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showNotification() {
        Log.e("Notification: ", "Show-Notification-Methode");
        Intent notificationIntent = new Intent(this, gps_service.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new Notification.Builder(this)
                .setContentTitle(getText(R.string.app_name))
                .setContentText("hallo")
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentIntent(pendingIntent)
                .setTicker("Peter")
                .build();

        startForeground(101, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) locationManager.removeUpdates(locationListener);
    }
}