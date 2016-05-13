package fr.pds.isintheair.phonintheair.model.websocket;

import android.util.Log;

import de.tavendo.autobahn.WebSocketConnectionHandler;
import fr.pds.isintheair.phonintheair.controller.bus.event.CalendarWebSocketConnectionLostEvent;
import fr.pds.isintheair.phonintheair.controller.bus.event.CalendarWebSocketConnectionRetrievedEvent;
import fr.pds.isintheair.phonintheair.controller.bus.handler.BusHandlerSingleton;
import fr.pds.isintheair.phonintheair.controller.message.CalendarMessageController;
import fr.pds.isintheair.phonintheair.helper.JSONHelper;
import fr.pds.isintheair.phonintheair.model.entity.CalendarMessage;

/******************************************
 * Created by        : jdatour            *
 * Creation date     : 03/19/16           *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

public class CalendarWebsocketHandler extends WebSocketConnectionHandler {
    private String TAG = getClass().getSimpleName();

    @Override
    public void onOpen() {
        Log.d(TAG, "Calendar session opened");

        WebSocketConnectionHandlerSingleton.getInstance().setIsCalendarConnected(true);
        BusHandlerSingleton.getInstance().getBus().post(new CalendarWebSocketConnectionRetrievedEvent());
        CalendarMessageController.sendRegisterMessage();
    }

    @Override
    public void onClose(int code, String reason) {
        Log.d(TAG, "Calendar session closed with code : " + code + " with reason : " + reason);

        WebSocketConnectionHandlerSingleton.getInstance().setIsCalendarConnected(false);
        BusHandlerSingleton.getInstance().getBus().post(new CalendarWebSocketConnectionLostEvent());

        WebSocketConnectionHandlerSingleton.getInstance().connectToCalendar();
    }

    @Override
    public void onTextMessage(String payload) {
        Log.d(TAG, "Calendar message received : " + payload);

        if (!payload.isEmpty()) {
            CalendarMessageController.handleMessage((CalendarMessage) JSONHelper.deserialize(payload, CalendarMessage.class));
        }
    }
}
