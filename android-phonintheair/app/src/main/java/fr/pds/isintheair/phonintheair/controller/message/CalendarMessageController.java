package fr.pds.isintheair.phonintheair.controller.message;

import java.util.List;

import fr.pds.isintheair.phonintheair.PhonintheairApp;
import fr.pds.isintheair.phonintheair.helper.SharedPreferencesHelper;
import fr.pds.isintheair.phonintheair.model.entity.CalendarMessage;
import fr.pds.isintheair.phonintheair.model.entity.DeviceType;
import fr.pds.isintheair.phonintheair.model.entity.Event;
import fr.pds.isintheair.phonintheair.model.entity.MessageInfo;
import fr.pds.isintheair.phonintheair.model.entity.MessageType;
import fr.pds.isintheair.phonintheair.model.entity.NotificationType;
import fr.pds.isintheair.phonintheair.model.entity.SessionInfo;
import fr.pds.isintheair.phonintheair.model.provider.CalendarProvider;
import fr.pds.isintheair.phonintheair.model.websocket.WebSocketConnectionHandlerSingleton;

/******************************************
 * Created by        : jdatour            *
 * Creation date     : 03/19/16           *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

public class CalendarMessageController {
    public static void handleMessage(CalendarMessage message) {
        switch (message.getMessageInfo().getMessageType()) {
            case CALENDAR_FULL_SYNC:
                sendFullSyncMessage();
                break;
        }
    }

    public static void sendFullSyncMessage() {
        Long            agendaId        = SharedPreferencesHelper.readLong("agendaId", 42);
        Integer         userId          = SharedPreferencesHelper.readInteger("userId", 0);
        List<Event>     events          = new CalendarProvider(PhonintheairApp.context).getEvents(agendaId);
        MessageInfo     messageInfo     = new MessageInfo.Builder().addMessageType(MessageType.CALENDAR_FULL_SYNC).build();
        SessionInfo     sessionInfo     = new SessionInfo.Builder().addDeviceType(DeviceType.PHONE).addNotificationType(NotificationType.CALENDAR).addUserId(userId).build();
        CalendarMessage calendarMessage = new CalendarMessage.Builder().addMessageInfo(messageInfo).addSessionInfo(sessionInfo).addEvents(events).build();

        WebSocketConnectionHandlerSingleton.getInstance().sendMessage(calendarMessage);
    }

    public static void sendRegisterMessage() {
        Integer         userId      = SharedPreferencesHelper.readInteger("userId", 0);
        MessageInfo     messageInfo = new MessageInfo.Builder().addMessageType(MessageType.REGISTER).build();
        SessionInfo     sessionInfo = new SessionInfo.Builder().addDeviceType(DeviceType.PHONE).addNotificationType(NotificationType.CALENDAR).addUserId(userId).build();
        CalendarMessage message     = new CalendarMessage.Builder().addMessageInfo(messageInfo).addSessionInfo(sessionInfo).build();

        WebSocketConnectionHandlerSingleton.getInstance().sendMessage(message);
    }
}
