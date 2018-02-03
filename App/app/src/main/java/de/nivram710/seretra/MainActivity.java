package de.nivram710.seretra;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location changedLocation;

    private static final int PERMISSIONS_ACCESS_FINE_LOCATION = 1;
    TextView koordinatenText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        koordinatenText = findViewById(R.id.koordinaten);
        koordinatenText.setText(R.string.app_name);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                changedLocation = location;
                showLocation(changedLocation);
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
                startActivity(intent);
            }
        };

        startLocation();

        if (getLocation() != null) Log.e("MainActivity/if", getLocation().toString());
        else Log.e("MainActivity/else", "getLocation = null");

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e("Peter", permissions[0]);
        switch (requestCode) {
            case PERMISSIONS_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, getString(R.string.gps_permission_user_accept), Toast.LENGTH_LONG).show();
                    locationManager.requestLocationUpdates("gps", 1000, 0, locationListener);
                } else {
                    Toast.makeText(this, getString(R.string.gps_permission_user_denied), Toast.LENGTH_LONG).show();
                }
                break;
        }

    }

    private boolean isPermissionsGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @SuppressLint("MissingPermission")
    private void askForPermission() {
        if (!(isPermissionsGranted())) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_ACCESS_FINE_LOCATION);
        } else {
            locationManager.requestLocationUpdates("gps", 1000, 1, locationListener);
        }
    }

    @SuppressLint("SetTextI18n")
    public void showLocation(Location location) {
        String latitudeDirection;
        String longtitudeDirection;

        // Überprüfen ob nördlich oder südlich
        if(location.getLatitude() > 0) latitudeDirection = "N";
        else latitudeDirection = "S";

        // Überprüfen ob westlich oder östlich
        if(location.getLongitude() > 0) longtitudeDirection = "E";
        else longtitudeDirection = "W";

        koordinatenText.setText(latitudeDirection + location.getLatitude() + "°\n" + longtitudeDirection + location.getLongitude() + "°");

    }

    @SuppressLint("MissingPermission")
    private void startLocation() {
        askForPermission();
    }

    private Location getLocation() {
        return changedLocation;
    }

    // todo: run / keep running when closed
    // todo: sendLocationToServer()
}