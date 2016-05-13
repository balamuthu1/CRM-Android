package fr.pds.isintheair.notifier.controller;

import fr.pds.isintheair.notifier.entity.*;
import fr.pds.isintheair.notifier.helper.JSONHelper;

import javax.websocket.Session;
import java.util.List;

/******************************************
 * Created by        : jdatour            *
 * Creation date     : 03/19/16           *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

public class CalendarController {
    public static void sendFullSync (SessionInfo sessionInfo, List<Event> events) {
        Session                 peerSession = PeerHandlerSingleton.getInstance().findPeerSession(sessionInfo);
        MessageInfo             messageInfo = new MessageInfo.Builder().addMessageType(MessageType.CALENDAR_FULL_SYNC).build();
        CalendarMessage.Builder builder     = new CalendarMessage.Builder().addMessageMeta(messageInfo);

        if (sessionInfo.getDeviceType().equals(DeviceType.PHONE)) {
            builder.addEvents(events);
        }

        CalendarMessage calendarMessage = builder.build();

        peerSession.getAsyncRemote().sendText(JSONHelper.serialize(calendarMessage, CalendarMessage.class));
    }
}
