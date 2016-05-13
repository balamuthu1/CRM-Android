package fr.pds.isintheair.notifier.server;

import fr.pds.isintheair.notifier.controller.CalendarMessageController;
import fr.pds.isintheair.notifier.controller.PeerHandlerSingleton;
import fr.pds.isintheair.notifier.entity.CalendarMessage;
import fr.pds.isintheair.notifier.helper.JSONHelper;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.logging.Logger;

/******************************************
 * Created by        : jdatour            *
 * Creation date     : 03/19/16           *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

@ServerEndpoint("/calendar")
public class CalendarNotifierEndpoint {
    private final static Logger logger = Logger.getLogger(CalendarNotifierEndpoint.class.getName());

    @OnOpen
    public void onOpen (Session session) throws IOException {
        logger.info("Calendar session opened");
    }

    @OnError
    public void onError (Session session, Throwable throwable) {
        logger.info("Calendar error : " + throwable.getMessage());
    }

    @OnMessage()
    public void onMessage (String body, Session session) throws IOException {
        logger.info("Calendar message received : " + body);

        CalendarMessage calendarMessage = (CalendarMessage) JSONHelper.deserialize(body, CalendarMessage.class);

        CalendarMessageController.handleMessage(calendarMessage, session);
    }

    @OnClose
    public void onClose (Session session, CloseReason closeReason) {
        logger.info("Calendar closed session : " + closeReason.getReasonPhrase());

        PeerHandlerSingleton.getInstance().removePeerSession(session);
    }
}
