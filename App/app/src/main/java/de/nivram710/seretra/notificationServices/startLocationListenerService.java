package de.nivram710.seretra.notificationServices;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import de.nivram710.seretra.gps_service;

public class startLocationListenerService extends Service {
    public startLocationListenerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
