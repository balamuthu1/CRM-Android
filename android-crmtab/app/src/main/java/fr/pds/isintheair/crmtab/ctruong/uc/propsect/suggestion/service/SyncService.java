package fr.pds.isintheair.crmtab.ctruong.uc.propsect.suggestion.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import fr.pds.isintheair.crmtab.ctruong.uc.propsect.suggestion.adapter.SyncAdapter;

/**
 *
 */
public class SyncService extends Service {

    // Storage for an instance of the sync adapter
    private static SyncAdapter sSyncAdapter = null;
    // Object to use as a thread-safe lock
    private static final Object sSyncAdapterLock = new Object();
    private static final String TAG = "SyncService";
    /**
     * Instantiate the sync adapter object.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        /*
         * Create the sync adapter as a singleton.
         * Set the sync adapter as syncable
         * Disallow parallel syncs
         */
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    /**
     * Logging-only destructor.
     */
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Service destroyed");
    }


    /**
     * Return an object that allows the system to invoke
     * the sync adapter.
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {

//         Get the object that allows external processes
//         to call onPerformSync(). The object is created
//         in the base class code when the SyncAdapter
//         constructors call super()

        return sSyncAdapter.getSyncAdapterBinder();
    }
}
