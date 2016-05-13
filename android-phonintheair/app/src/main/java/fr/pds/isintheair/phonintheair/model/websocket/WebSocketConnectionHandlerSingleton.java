package fr.pds.isintheair.phonintheair.model.websocket;

import android.util.Log;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import fr.pds.isintheair.phonintheair.helper.JSONHelper;
import fr.pds.isintheair.phonintheair.model.constant.Constant;
import fr.pds.isintheair.phonintheair.model.entity.CalendarMessage;
import fr.pds.isintheair.phonintheair.model.entity.CallMessage;
import fr.pds.isintheair.phonintheair.model.entity.Message;

public class WebSocketConnectionHandlerSingleton {
    private static WebSocketConnectionHandlerSingleton INSTANCE                    = null;
    private        String                              TAG                         = getClass().getSimpleName();
    private        WebSocketConnection                 calendarWebsocketConnection = null;
    private        WebSocketConnection                 callWebsocketConnection     = null;
    private        Boolean                             isCallConnected             = false;
    private        Boolean                             isCalendarConnected         = false;

    private WebSocketConnectionHandlerSingleton() {
        calendarWebsocketConnection = new WebSocketConnection();
        callWebsocketConnection = new WebSocketConnection();
    }

    public static synchronized WebSocketConnectionHandlerSingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new WebSocketConnectionHandlerSingleton();
        }

        return INSTANCE;
    }

    /**
     * Connect to web socket server
     */
    public void connectToCall() {
        CallWebSocketHandler callWebSocketHandler = new CallWebSocketHandler();

        try {
            callWebsocketConnection.connect(Constant.WEBSOCKET_CALL_ENDPOINT, callWebSocketHandler);
        }
        catch (WebSocketException e) {
            Log.d(TAG, "Call websocket connection failed : " + e.getMessage());
            //TODO handle exception
        }
    }

    public void connectToCalendar() {
        CalendarWebsocketHandler calendarWebsocketHandler = new CalendarWebsocketHandler();

        try {
            calendarWebsocketConnection.connect(Constant.WEBSOCKET_CALENDAR_ENDPOINT, calendarWebsocketHandler);
        }
        catch (WebSocketException e) {
            Log.d(TAG, "Calendar websocket connection failed : " + e.getMessage());
            //TODO handle exception
        }
    }

    /**
     * Send a message using websocket
     *
     * @param message the message to send
     */
    public void sendMessage(Message message) {
        String serializedMessage = JSONHelper.serialize(message, message.getClass());

        if (message instanceof CalendarMessage) {
            calendarWebsocketConnection.sendTextMessage(serializedMessage);
        }

        else if (message instanceof CallMessage) {
            callWebsocketConnection.sendTextMessage(serializedMessage);
        }

        Log.d(TAG, "Sending : " + serializedMessage);
    }

    public Boolean getIsCallConnected() {
        return isCallConnected;
    }

    public void setIsCallConnected(Boolean isCallConnected) {
        this.isCallConnected = isCallConnected;
    }

    public Boolean getIsCalendarConnected() {
        return isCalendarConnected;
    }

    public void setIsCalendarConnected(Boolean isCalendarConnected) {
        this.isCalendarConnected = isCalendarConnected;
    }
}
