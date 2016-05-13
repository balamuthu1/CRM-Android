package fr.pds.isintheair.crmtab.controller.message;

import java.util.List;

import fr.pds.isintheair.crmtab.controller.bus.BusHandlerSingleton;
import fr.pds.isintheair.crmtab.controller.bus.event.CalendarFullSyncEvent;
import fr.pds.isintheair.crmtab.model.dao.EventDAO;
import fr.pds.isintheair.crmtab.model.dao.UserDAO;
import fr.pds.isintheair.crmtab.model.entity.CalendarMessage;
import fr.pds.isintheair.crmtab.model.entity.DeviceType;
import fr.pds.isintheair.crmtab.model.entity.Event;
import fr.pds.isintheair.crmtab.model.entity.MessageInfo;
import fr.pds.isintheair.crmtab.model.entity.MessageType;
import fr.pds.isintheair.crmtab.model.entity.NotificationType;
import fr.pds.isintheair.crmtab.model.entity.SessionInfo;
import fr.pds.isintheair.crmtab.model.websocket.WebSocketConnectionHandlerSingleton;

/******************************************
 * Created by        : jdatour            *
 * Creation date     : 03/19/16           *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

public class CalendarMessageController {
    public static void handleMessage(CalendarMessage calendarMessage) {
        switch (calendarMessage.getMessageInfo().getMessageType()) {
            case CALENDAR_FULL_SYNC:
                handleFullSync(calendarMessage.getEvents());
                break;
        }
    }

    public static void sendRegisterMessage() {
        Integer         userId      = UserDAO.getCurrentUser().getEmail().hashCode();
        MessageInfo     messageInfo = new MessageInfo.Builder().addMessageType(MessageType.REGISTER).build();
        SessionInfo     sessionInfo = new SessionInfo.Builder().addDeviceType(DeviceType.TABLET).addNotificationType(NotificationType.CALENDAR).addUserId(userId).build();
        CalendarMessage message     = new CalendarMessage.Builder().addMessageInfo(messageInfo).addSessionInfo(sessionInfo).build();

        WebSocketConnectionHandlerSingleton.getInstance().sendMessage(message);
    }

    public static void sendFullSyncMessage() {
        Integer         userId      = UserDAO.getCurrentUser().getEmail().hashCode();
        MessageInfo     messageInfo = new MessageInfo.Builder().addMessageType(MessageType.CALENDAR_FULL_SYNC).build();
        SessionInfo     sessionInfo = new SessionInfo.Builder().addDeviceType(DeviceType.TABLET).addNotificationType(NotificationType.CALENDAR).addUserId(userId).build();
        CalendarMessage message     = new CalendarMessage.Builder().addMessageInfo(messageInfo).addSessionInfo(sessionInfo).build();

        WebSocketConnectionHandlerSingleton.getInstance().sendMessage(message);
    }

    private static void handleFullSync(List<Event> events) {
        List<Event> databaseEvents = EventDAO.getAll();

        for (Event event : databaseEvents) {
            event.delete();
        }

        for (Event event : events) {
            event.save();
        }

        BusHandlerSingleton.getInstance().getBus().post(new CalendarFullSyncEvent(events));
    }
}
