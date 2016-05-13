package fr.pds.isintheair.crmtab.syncContact;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ContactSyncService extends Service {

    private static ContactSyncAdapter sSyncAdapter = null;
    // Object to use as a thread-safe lock
    private static final Object sSyncAdapterLock = new Object();

    @Override
    public void onCreate() {
        super.onCreate();
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new ContactSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    public ContactSyncService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       return sSyncAdapter.getSyncAdapterBinder();
    }
}
