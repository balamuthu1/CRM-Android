package fr.pds.isintheair.crmtab.controller.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import fr.pds.isintheair.crmtab.model.websocket.WebSocketConnectionHandlerSingleton;

public class CallService extends Service {
    @Nullable
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
