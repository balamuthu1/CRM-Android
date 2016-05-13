package fr.pds.isintheair.phonintheair.model.websocket;

import android.util.Log;

import de.tavendo.autobahn.WebSocketConnectionHandler;
import fr.pds.isintheair.phonintheair.controller.bus.event.CallWebSocketConnectionLostEvent;
import fr.pds.isintheair.phonintheair.controller.bus.event.CallWebSocketConnectionRetrievedEvent;
import fr.pds.isintheair.phonintheair.controller.bus.handler.BusHandlerSingleton;
import fr.pds.isintheair.phonintheair.controller.message.CallMessageController;
import fr.pds.isintheair.phonintheair.helper.JSONHelper;
import fr.pds.isintheair.phonintheair.model.entity.CallMessage;

/******************************************
 * Created by        : jdatour            *
 * Creation date     : 03/19/16           *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

public class CallWebSocketHandler extends WebSocketConnectionHandler {
    private String TAG = getClass().getSimpleName();

    @Override
    public void onOpen() {
        Log.d(TAG, "Call session opened");

        WebSocketConnectionHandlerSingleton.getInstance().setIsCallConnected(true);
        BusHandlerSingleton.getInstance().getBus().post(new CallWebSocketConnectionRetrievedEvent());
        CallMessageController.sendRegisterMessage();
    }

    @Override
    public void onClose(int code, String reason) {
        Log.d(TAG, "Call session closed with code : " + code + " with reason : " + reason);

        WebSocketConnectionHandlerSingleton.getInstance().setIsCallConnected(false);
        BusHandlerSingleton.getInstance().getBus().post(new CallWebSocketConnectionLostEvent());

        WebSocketConnectionHandlerSingleton.getInstance().connectToCall();
    }

    @Override
    public void onTextMessage(String payload) {
        Log.d(TAG, "Call message received : " + payload);

        if (!payload.isEmpty()) {
            CallMessageController.handleMessage((CallMessage) JSONHelper.deserialize(payload, CallMessage.class));
        }
    }
}

