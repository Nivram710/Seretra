package de.nivram710.seretra;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

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
            public void onLocationChanged(Location location) {
                Intent intent = new Intent("location_update");
                intent.putExtra("location", location.getLatitude() + ";" + location.getLongitude());
                sendLocationToServer(location);
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

    }

    // todo: sendLocationToServer()
    public void sendLocationToServer(Location location) {

        String longtitude = String.valueOf(location.getLongitude());
        String latitude = String.valueOf(location.getLatitude());
        String upload_url_String = "http://nivram710.ddns.net/upload.php";
        String update_url_String = "http://nivram710.ddns.net/update.php";
        StringBuilder result = new StringBuilder();
        String line;

        try {
            URL upload_url = new URL(upload_url_String);
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) upload_url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UFT-8"));
                String psot_data = URLEncoder.encode("longtitude", "UTF-8") + "=" + URLEncoder.encode("longtitude", longtitude) + "&"
                        + URLEncoder.encode("latitude", "UTF-8") + "=" + URLEncoder.encode("latitude", latitude) + "&"
                        + URLEncoder.encode("imei", "UTF-8") + "=" + URLEncoder.encode("imei", String.valueOf(3));
                bufferedWriter.write(psot_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();


            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();

        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) locationManager.removeUpdates(locationListener);
    }
}
