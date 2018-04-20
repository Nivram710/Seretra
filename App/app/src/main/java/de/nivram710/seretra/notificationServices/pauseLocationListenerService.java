package de.nivram710.seretra.notificationServices;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("Registered")
public class pauseLocationListenerService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("ShowToast")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
