package fr.pds.isintheair.phonintheair;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import fr.pds.isintheair.phonintheair.controller.message.CalendarMessageController;
import fr.pds.isintheair.phonintheair.helper.SharedPreferencesHelper;
import fr.pds.isintheair.phonintheair.model.entity.CalendarMessage;
import fr.pds.isintheair.phonintheair.model.websocket.CalendarWebsocketHandler;
import fr.pds.isintheair.phonintheair.model.websocket.WebSocketConnectionHandlerSingleton;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
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
@PrepareForTest({CalendarMessageController.class, SharedPreferencesHelper.class, WebSocketConnectionHandlerSingleton.class})
public class CalendarWebsocketHandlerTest {
    @Test
    public void testRegisterCalledOnOpen() {
        PowerMockito.mockStatic(CalendarMessageController.class);
        PowerMockito.mockStatic(SharedPreferencesHelper.class);

        PowerMockito.when(SharedPreferencesHelper.readInteger(anyString(), anyInt())).thenReturn(42);

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
