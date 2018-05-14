package de.nivram710.seretra.notificationServices;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import de.nivram710.seretra.MainActivity;
import de.nivram710.seretra.R;
import de.nivram710.seretra.gps_service;

public class stopLocationListenerService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Die Methode beendet die App mit allen Services und Applications
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Intent gpsServiceIntent = new Intent(getApplicationContext(), gps_service.class);
        Intent mainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);

        this.stopService(gpsServiceIntent);
        this.onTaskRemoved(mainActivityIntent);

        android.os.Process.killProcess(android.os.Process.myPid());

        this.stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * Wenn sich der Stopp-Service selber beendet, gibt er nocht eine Warnung aus,
     * dass Seretra nicht mehr aktiv ist
     */
    @Override
    public void onDestroy() {
        Toast.makeText(getApplicationContext(), getText(R.string.toast_warning_stop), Toast.LENGTH_LONG).show();
        super.onDestroy();
    }
}
