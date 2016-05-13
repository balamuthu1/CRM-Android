package fr.pds.isintheair.notifier;

import fr.pds.isintheair.notifier.controller.CallMessageController;
import fr.pds.isintheair.notifier.controller.PeerHandlerSingleton;
import fr.pds.isintheair.notifier.entity.DeviceType;
import fr.pds.isintheair.notifier.entity.NotificationType;
import fr.pds.isintheair.notifier.entity.SessionInfo;
import fr.pds.isintheair.notifier.server.CallNotifierEndpoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.websocket.CloseReason;
import javax.websocket.Session;
import java.io.IOException;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doNothing;

/******************************************
 * Created by        : jdatour            *
 * Creation date     : 03/23/16           *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

@RunWith(PowerMockRunner.class)
@PrepareForTest({CallMessageController.class})
public class CallNotifierEndpointTest {
    @Test
    public void testSessionIsAddedWhenOpening () throws IOException {
        PeerHandlerSingleton peerHandlerSingleton = PowerMockito.spy(PeerHandlerSingleton.getInstance());
        Session              session              = mock(Session.class);
        SessionInfo          sessionInfo          = mock(SessionInfo.class);

        when(sessionInfo.getDeviceType()).thenReturn(DeviceType.PHONE);
        when(sessionInfo.getNotificationType()).thenReturn(NotificationType.CALL);
        when(sessionInfo.getUserId()).thenReturn(0);
        doNothing().when(peerHandlerSingleton).addPeer(session, sessionInfo);

        CallNotifierEndpoint callNotifierEndpoint = new CallNotifierEndpoint();
        callNotifierEndpoint.onOpen(session);

        PowerMockito.verifyStatic();
        peerHandlerSingleton.addPeer(session, sessionInfo);
    }


    @Test
    public void testMessageIsHandledWhenReceiving () throws Exception {
        PowerMockito.mockStatic(CallMessageController.class);

        CallNotifierEndpoint callNotifierEndpoint = new CallNotifierEndpoint();
        callNotifierEndpoint.onMessage(anyString(), anyObject());

        PowerMockito.verifyStatic();
        CallMessageController.handleMessage(anyObject(), anyObject());
    }

    @Test
    public void testSessionIsRemovedFromPeeringWhenClosing () throws Exception {
        PeerHandlerSingleton peerHandlerSingleton = PowerMockito.spy(PeerHandlerSingleton.getInstance());
        CloseReason          closeReason          = mock(CloseReason.class);
        Session              session              = mock(Session.class);

        CallNotifierEndpoint callNotifierEndpoint = new CallNotifierEndpoint();
        callNotifierEndpoint.onClose(session, closeReason);

        doNothing().when(peerHandlerSingleton).removePeerSession(session);

        PowerMockito.verifyStatic();
        peerHandlerSingleton.removePeerSession(session);
    }
}