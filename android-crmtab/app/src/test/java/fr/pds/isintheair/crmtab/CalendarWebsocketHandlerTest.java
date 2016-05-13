package fr.pds.isintheair.crmtab;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import fr.pds.isintheair.crmtab.controller.message.CalendarMessageController;
import fr.pds.isintheair.crmtab.model.dao.UserDAO;
import fr.pds.isintheair.crmtab.model.entity.CalendarMessage;
import fr.pds.isintheair.crmtab.model.websocket.CalendarWebsocketHandler;
import fr.pds.isintheair.crmtab.model.websocket.WebSocketConnectionHandlerSingleton;

import static org.mockito.Matchers.isA;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

/******************************************
 * Created by        :                    *
 * Creation date     : 03/23/16            *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

@RunWith(PowerMockRunner.class)
@PrepareForTest({CalendarMessageController.class, UserDAO.class, WebSocketConnectionHandlerSingleton.class})
public class CalendarWebsocketHandlerTest {
    @Test
    public void testRegisterCalledOnOpen() {
        PowerMockito.mockStatic(CalendarMessageController.class);

        CalendarWebsocketHandler calendarWebsocketHandler = new CalendarWebsocketHandler();
        calendarWebsocketHandler.onOpen();

        verifyStatic();
        CalendarMessageController.sendRegisterMessage();
    }

    @Test
    public void testTryToReconnectWhenConnectionLost() {
        WebSocketConnectionHandlerSingleton webSocketConnectionHandlerSingleton = PowerMockito.spy(WebSocketConnectionHandlerSingleton.getInstance());

        doNothing().when(webSocketConnectionHandlerSingleton).connectToCall();

        CalendarWebsocketHandler calendarWebsocketHandler = new CalendarWebsocketHandler();
        calendarWebsocketHandler.onClose(0, "");

        verifyStatic();
        webSocketConnectionHandlerSingleton.connectToCall();
    }

    @Test
    public void testMessageIshandledWhenReceived() {
        PowerMockito.mockStatic(CalendarMessageController.class);

        CalendarWebsocketHandler calendarWebsocketHandler = new CalendarWebsocketHandler();
        calendarWebsocketHandler.onTextMessage(
                "{\"messageInfo\":{\"messageType\":\"REGISTER\"},\"sessionInfo\":{\"deviceType\":\"TABLET\",\"notificationType\":\"CALENDAR\",\"userId\":1851270730}}");

        verifyStatic();
        CalendarMessageController.handleMessage(isA(CalendarMessage.class));
    }

}
