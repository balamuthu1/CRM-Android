package fr.pds.isintheair.notifier.controller;

import fr.pds.isintheair.notifier.entity.CalendarMessage;

import javax.websocket.Session;

/******************************************
 * Created by        : jdatour            *
 * Creation date     : 03/19/16           *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

public class CalendarMessageController {
    public static void handleMessage (CalendarMessage calendarMessage, Session session) {
        switch (calendarMessage.getMessageInfo().getMessageType()) {
            case REGISTER:
                PeerHandlerSingleton.getInstance().addPeer(session, calendarMessage.getSessionInfo());
                break;
            case CALENDAR_FULL_SYNC:
                CalendarController.sendFullSync(calendarMessage.getSessionInfo(), calendarMessage.getEvents());
                break;
        }
    }
}
