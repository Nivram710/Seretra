package de.nivram710.seretra;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;

    private static final int PERMISSIONS_ACCESS_FINE_LOCATION = 1;
    TextView koordinatenText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        koordinatenText = findViewById(R.id.koordinaten);
        koordinatenText.setText(R.string.app_name);

        startLocation();

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, getString(R.string.gps_permission_user_accept), Toast.LENGTH_LONG).show();
                    startGPSService();
                } else {
                    Toast.makeText(this, getString(R.string.gps_permission_user_denied), Toast.LENGTH_LONG).show();
                }
                break;
        }

    }

    public boolean isPermissionsGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @SuppressLint("MissingPermission")
    public void askForPermission() {
        if (!(isPermissionsGranted())) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_ACCESS_FINE_LOCATION);
        } else {
            startGPSService();
        }
    }

    private void startGPSService() {
        Intent gps_service_intent = new Intent(getApplicationContext(), gps_service.class);
        startService(gps_service_intent);
    }

    @SuppressLint("SetTextI18n")
    public void showLocation(String location) {
        koordinatenText.setText(location);
    }

    @SuppressLint("MissingPermission")
    private void startLocation() {
        askForPermission();
        startGPSService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    showLocation("" + intent.getExtras().get("location"));
                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("location_update"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null) unregisterReceiver(broadcastReceiver);
    }

    // todo: sendLocationToServer()
}