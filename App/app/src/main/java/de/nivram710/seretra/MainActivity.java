package de.nivram710.seretra;
import android.Manifest;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Die Klasse startet den GPS-Service, verwaltet die von dem
 * Service gesammelten Daten und zeigt diese an.
 */
// todo: anti chrash at ask for Permissions (maybe wait)
public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;

    private static final int PERMISSIONS_ACCESS_FINE_LOCATION = 1;
    private static final int PERMISSIONS_READ_PHONE_STATE = 2;
    TextView koordinatenText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        koordinatenText = findViewById(R.id.koordinaten);
        koordinatenText.setText(R.string.app_name);

        askForPermission();
        startGPSService();

    }

    @SuppressLint({"MissingPermission", "ShowToast"})
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
            case PERMISSIONS_READ_PHONE_STATE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Indetifikationsnummer darf gelesen werden!", Toast.LENGTH_LONG);
                } else {
                    Toast.makeText(this, "Wir brauchen die Erlaubnis um sie Indetifizieren zu können", Toast.LENGTH_LONG);
                }
                break;
        }

    }

    /**
     * Überprüft ob die Berechtigung zur Koordianten ausgelesen werden dürfen
     */
    public boolean isPermissionsGrantedAccessLocation() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean isPermissionGrantedReadPhoneState() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Die Methode überprüft, ob die benötigeten Permissions erhalten worden sind.
     *
     * @retun void
     */
    @SuppressLint("MissingPermission")
    public void askForPermission() {
        if (!(isPermissionsGrantedAccessLocation())) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_ACCESS_FINE_LOCATION);
        }
        if (!(isPermissionGrantedReadPhoneState())) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
                    PERMISSIONS_READ_PHONE_STATE);
        }
    }

    /**
     * Die Methode startet den GPS-Service
     *
     * @return void
     */
    private void startGPSService() {
        Intent gps_service_intent = new Intent(getApplicationContext(), gps_service.class);
        startService(gps_service_intent);
    }

    /**
     * Die Methode zeigt die vom Service gesammelten Daten an.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"SetTextI18n", "MissingPermission"})
    public void showLocation(String location) {

        String titudes[] = location.split(";");
        String latitudeString = titudes[0];
        String longtitudeString = titudes[1];

        if (latitudeString.contains("-")) {
            latitudeString = "S" + latitudeString.replace("-", "");
        } else {
            latitudeString = "N" + latitudeString;
        }

        if (longtitudeString.contains("-")) {
            longtitudeString = "W" + longtitudeString.replace("-", "");
        } else {
            longtitudeString = "E" + longtitudeString;
        }

        koordinatenText.setText(latitudeString + "\n" + longtitudeString);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @SuppressWarnings("ConstantConditions")
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
        if (broadcastReceiver != null) unregisterReceiver(broadcastReceiver);
    }

}