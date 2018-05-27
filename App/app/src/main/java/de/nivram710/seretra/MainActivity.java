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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Die Klasse startet den GPS-Service, verwaltet die von dem
 * Service gesammelten Daten und zeigt diese an.
 */
public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;

    int permission_all = 1;
    String[] neededPermissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET, Manifest.permission.READ_PHONE_STATE};

    TextView koordinatenText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        koordinatenText = findViewById(R.id.koordinaten);
        koordinatenText.setText(R.string.app_name);

        if(!hasPermissions(this, neededPermissions)) {
            ActivityCompat.requestPermissions(this, neededPermissions, permission_all);
        } else {
            startGPSService();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        startGPSService();
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Die Methode startet den GPS-Service
     */
    @SuppressLint("SetTextI18n")
    private void startGPSService() {
        if (hasPermissions(this, neededPermissions)) {
            Intent gps_service_intent = new Intent(getApplicationContext(), gps_service.class);
            startService(gps_service_intent);
        } else {
            koordinatenText.setTextSize(12);
            koordinatenText.setText(getText(R.string.no_permissions)+ "\n" + getText(R.string.how_fix_problem));
        }
    }

    /**
     * Die Methode zeigt die vom Service gesammelten Daten an.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"SetTextI18n", "MissingPermission"})
    public void showLocation(String location) {

        String positionData[] = location.split(";");
        String latitudeString = positionData[0];
        String longtitudeString = positionData[1];
        String altitudeString = positionData[2];

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

        altitudeString = "H: " + altitudeString;

        koordinatenText.setText(latitudeString + "\n" + longtitudeString+ "\n" + altitudeString);

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