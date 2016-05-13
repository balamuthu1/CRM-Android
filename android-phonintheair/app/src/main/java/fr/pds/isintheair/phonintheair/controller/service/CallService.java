package fr.pds.isintheair.phonintheair.controller.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import fr.pds.isintheair.phonintheair.model.websocket.WebSocketConnectionHandlerSingleton;

/******************************************
 * Created by        : jdatour            *
 * Creation date     : 01/24/16           *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

public class CallService extends Service {
    private String TAG = getClass().getName();

    @Override
    public IBinder onBind(Intent intent) {
        return new CallBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        WebSocketConnectionHandlerSingleton.getInstance().connectToCall();

        return START_STICKY;
    }

    public class CallBinder extends Binder {
        CallService getService() {
            return CallService.this;
        }
    }
}
