package fr.pds.isintheair.crmtab;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import fr.pds.isintheair.crmtab.controller.message.CallMessageController;
import fr.pds.isintheair.crmtab.model.entity.CallMessage;
import fr.pds.isintheair.crmtab.model.websocket.CallWebSocketHandler;
import fr.pds.isintheair.crmtab.model.websocket.WebSocketConnectionHandlerSingleton;

import static org.mockito.Matchers.isA;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CallMessageController.class, WebSocketConnectionHandlerSingleton.class})
public class CallWebSocketHandlerTest {
    @Test
    public void testRegisterCalledOnOpen() {
        PowerMockito.mockStatic(CallMessageController.class);

        CallWebSocketHandler callWebSocketHandler = new CallWebSocketHandler();
        callWebSocketHandler.onOpen();

        verifyStatic();
        CallMessageController.sendRegisterMessage();
    }

    @Test
    public void testTryToReconnectWhenConnectionLost() {
        WebSocketConnectionHandlerSingleton webSocketConnectionHandlerSingleton = PowerMockito.spy(WebSocketConnectionHandlerSingleton.getInstance());

        doNothing().when(webSocketConnectionHandlerSingleton).connectToCall();

        CallWebSocketHandler callWebSocketHandler = new CallWebSocketHandler();
        callWebSocketHandler.onClose(0, "");

        verifyStatic();
        webSocketConnectionHandlerSingleton.connectToCall();
    }

    @Test
    public void testMessageIshandledWhenReceived() {
        PowerMockito.mockStatic(CallMessageController.class);

        CallWebSocketHandler callWebSocketHandler = new CallWebSocketHandler();
        callWebSocketHandler.onTextMessage(
                "{\"messageInfo\":{\"messageType\":\"REGISTER\"},\"sessionInfo\":{\"deviceType\":\"TABLET\",\"notificationType\":\"CALENDAR\",\"userId\":1851270730}}");

        verifyStatic();
        CallMessageController.handleMessage(isA(CallMessage.class));
    }
}