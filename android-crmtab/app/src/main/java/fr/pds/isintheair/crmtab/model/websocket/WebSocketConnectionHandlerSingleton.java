package fr.pds.isintheair.crmtab.model.websocket;

import android.util.Log;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import fr.pds.isintheair.crmtab.Constant;
import fr.pds.isintheair.crmtab.CrmTabApplication;
import fr.pds.isintheair.crmtab.R;
import fr.pds.isintheair.crmtab.controller.bus.BusHandlerSingleton;
import fr.pds.isintheair.crmtab.controller.bus.event.PhoneCallFailedEvent;
import fr.pds.isintheair.crmtab.helper.JSONHelper;
import fr.pds.isintheair.crmtab.helper.NetworkHelper;
import fr.pds.isintheair.crmtab.model.entity.CalendarMessage;
import fr.pds.isintheair.crmtab.model.entity.CallMessage;
import fr.pds.isintheair.crmtab.model.entity.Message;

public class WebSocketConnectionHandlerSingleton {

    private static WebSocketConnectionHandlerSingleton INSTANCE                    = null;
    private        String                              TAG                         = getClass().getSimpleName();
    private        WebSocketConnection                 calendarWebsocketConnection = null;
    private        WebSocketConnection                 callWebsocketConnection     = null;

    private WebSocketConnectionHandlerSingleton() {
    }

    public static synchronized WebSocketConnectionHandlerSingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new WebSocketConnectionHandlerSingleton();
        }

        return INSTANCE;
    }

    public void connectToCall() {
        CallWebSocketHandler callWebSocketHandler = new CallWebSocketHandler();

        callWebsocketConnection = new WebSocketConnection();

        try {
            callWebsocketConnection.connect(Constant.WEBSOCKET_CALL_ENDPOINT, callWebSocketHandler);
            Log.d(TAG, "Websocket connection opened");
        }
        catch (WebSocketException e) {
            Log.d(TAG, "Websocket connection failed : " + e.getMessage());
            //TODO handle exception
        }
    }

    public void connectToCalendar() {
        CalendarWebsocketHandler calendarWebsocketHandler = new CalendarWebsocketHandler();

        calendarWebsocketConnection = new WebSocketConnection();

        try {
            calendarWebsocketConnection.connect(Constant.WEBSOCKET_CALENDAR_ENDPOINT, calendarWebsocketHandler);
        }
        catch (WebSocketException e) {
            Log.d(TAG, "Websocket connection failed : " + e.getMessage());
            //TODO handle exception
        }
    }

    public void sendMessage(Message message) {
        if (NetworkHelper.isNetworkAvailable()) {
            String serializedMessage = JSONHelper.serialize(message, message.getClass());

            if (calendarWebsocketConnection == null) {
                connectToCalendar();
            }

            if (callWebsocketConnection == null) {
                connectToCall();
            }

            if (message instanceof CalendarMessage) {
                calendarWebsocketConnection.sendTextMessage(serializedMessage);
            }

            else if (message instanceof CallMessage) {
                callWebsocketConnection.sendTextMessage(serializedMessage);
            }

            Log.d(TAG, "Sending message : " + serializedMessage);
        }
        else {
            String errorMessage = CrmTabApplication.context.getResources().getString(R.string.message_failed_no_internet);

            BusHandlerSingleton.getInstance().getBus().post(new PhoneCallFailedEvent(errorMessage));
        }
    }
}
