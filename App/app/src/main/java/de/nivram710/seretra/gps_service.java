package de.nivram710.seretra;

import android.annotation.SuppressLint;
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
import android.support.annotation.RequiresApi;
import android.telephony.TelephonyManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Die Klasse ist für die Ortung des Nutzers verantwortlich
 */
public class gps_service extends Service {

    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {

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
                    @SuppressLint("HardwareIds")
                    public void run() {
                        String getData;
                        String imei = "";

                        String longtitude = String.valueOf(location.getLongitude());
                        String latitude = String.valueOf(location.getLatitude());


                        String upload_url_String = "http://nivram710.ddns.net/upload.php";
                        String update_url_String = "http://nivram710.ddns.net/update.php";

                        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                        if (telephonyManager != null) {
                            imei = telephonyManager.getImei();
                        }

                        getData = "?longtitude=";
                        getData += longtitude;
                        getData += "&latitude=";
                        getData += latitude;
                        getData += "&imei=";
                        getData += imei;

                        try {

                            URL upload_url = new URL(upload_url_String + getData);
                            URL update_url = new URL(update_url_String + getData);

                            if (isKnown(imei)) {
                                HttpURLConnection httpURLConnectionUpdate = (HttpURLConnection) update_url.openConnection();
                                httpURLConnectionUpdate.setDoOutput(true);
                                httpURLConnectionUpdate.setDoInput(true);
                                InputStream inputStream = httpURLConnectionUpdate.getInputStream();
                                inputStream.close();
                                httpURLConnectionUpdate.disconnect();
                            } else {
                                HttpURLConnection httpURLConnectionUpload = (HttpURLConnection) upload_url.openConnection();
                                httpURLConnectionUpload.setDoOutput(true);
                                httpURLConnectionUpload.setDoInput(true);
                                InputStream inputStream = httpURLConnectionUpload.getInputStream();
                                inputStream.close();
                                httpURLConnectionUpload.disconnect();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                sendBroadcast(intent);
            }

            boolean isKnown(String imei) {
                String result = "";
                String check_url_String = "http://nivram710.ddns.net/check.php?imei=" + imei;

                try {

                    URL check_url = new URL(check_url_String);
                    HttpURLConnection httpURLConnectionCheck = (HttpURLConnection) check_url.openConnection();
                    httpURLConnectionCheck.setDoOutput(true);
                    httpURLConnectionCheck.setDoInput(true);
                    InputStream inputStream = httpURLConnectionCheck.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result = line;
                    }

                    return result.equals("true");

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
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

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) locationManager.removeUpdates(locationListener);
    }
}