package fr.pds.isintheair.notifier;

import fr.pds.isintheair.notifier.controller.PeerHandlerSingleton;
import fr.pds.isintheair.notifier.entity.DeviceType;
import fr.pds.isintheair.notifier.entity.NotificationType;
import fr.pds.isintheair.notifier.entity.SessionInfo;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.websocket.Session;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PeerHandlerSingletonTest {
    PeerHandlerSingleton peerHandlerSingleton;

    @Before
    public void setUp () {
        peerHandlerSingleton = peerHandlerSingleton.getInstance();
    }

    @Test
    public void testFirstPeering () {
        assertNotNull(peerHandlerSingleton.getPeers());
    }

    @Test
    public void testFirstPeeringWithCalendarPhoneAdded () {
        Session     session     = mock(Session.class);
        SessionInfo sessionInfo = new SessionInfo.Builder().addDeviceType(DeviceType.PHONE).addNotificationType(NotificationType.CALENDAR).addUserId(0).build();

        peerHandlerSingleton.addPeer(session, sessionInfo);

        assertTrue(peerHandlerSingleton.getPeers().size() == 1);
        assertNotNull(peerHandlerSingleton.getPeers().get(0));
        assertNull(peerHandlerSingleton.getPeers().get(0)[0]);
        assertNotNull(peerHandlerSingleton.getPeers().get(0)[1]);
        assertNotNull(peerHandlerSingleton.getPeers().get(0)[1].phone);
        assertNull(peerHandlerSingleton.getPeers().get(0)[1].tablet);
        assertTrue(peerHandlerSingleton.getPeers().get(0)[1].phone.equals(session));
    }

    @Test
    public void testFirstPeeringWithCalendarTabletAdded () {
        Session     session     = mock(Session.class);
        SessionInfo sessionInfo = new SessionInfo.Builder().addDeviceType(DeviceType.TABLET).addNotificationType(NotificationType.CALENDAR).addUserId(0).build();

        peerHandlerSingleton.addPeer(session, sessionInfo);

        assertTrue(peerHandlerSingleton.getPeers().size() == 1);
        assertNotNull(peerHandlerSingleton.getPeers().get(0));
        assertNull(peerHandlerSingleton.getPeers().get(0)[0]);
        assertNotNull(peerHandlerSingleton.getPeers().get(0)[1]);
        assertNotNull(peerHandlerSingleton.getPeers().get(0)[1].phone);
        assertNotNull(peerHandlerSingleton.getPeers().get(0)[1].tablet);
        assertTrue(peerHandlerSingleton.getPeers().get(0)[1].tablet.equals(session));
    }

    @Test
    public void testFirstPeeringWithCallPhoneAdded () {
        Session     session     = mock(Session.class);
        SessionInfo sessionInfo = new SessionInfo.Builder().addDeviceType(DeviceType.PHONE).addNotificationType(NotificationType.CALL).addUserId(0).build();

        peerHandlerSingleton.addPeer(session, sessionInfo);

        assertTrue(peerHandlerSingleton.getPeers().size() == 1);
        assertNotNull(peerHandlerSingleton.getPeers().get(0));
        assertNotNull(peerHandlerSingleton.getPeers().get(0)[0]);
        assertNotNull(peerHandlerSingleton.getPeers().get(0)[1]);
        assertNotNull(peerHandlerSingleton.getPeers().get(0)[1].phone);
        assertNotNull(peerHandlerSingleton.getPeers().get(0)[1].tablet);
        assertNull(peerHandlerSingleton.getPeers().get(0)[0].tablet);
        assertNotNull(peerHandlerSingleton.getPeers().get(0)[0].phone);
        assertTrue(peerHandlerSingleton.getPeers().get(0)[0].phone.equals(session));
    }

    @Test
    public void testFirstPeeringWithCallTabletAdded () {
        Session     session     = mock(Session.class);
        SessionInfo sessionInfo = new SessionInfo.Builder().addDeviceType(DeviceType.TABLET).addNotificationType(NotificationType.CALL).addUserId(0).build();

        peerHandlerSingleton.addPeer(session, sessionInfo);

        assertTrue(peerHandlerSingleton.getPeers().size() == 1);
        assertNotNull(peerHandlerSingleton.getPeers().get(0));
        assertNotNull(peerHandlerSingleton.getPeers().get(0)[0]);
        assertNotNull(peerHandlerSingleton.getPeers().get(0)[1]);
        assertNotNull(peerHandlerSingleton.getPeers().get(0)[1].phone);
        assertNotNull(peerHandlerSingleton.getPeers().get(0)[1].tablet);
        assertNotNull(peerHandlerSingleton.getPeers().get(0)[0].tablet);
        assertNotNull(peerHandlerSingleton.getPeers().get(0)[0].phone);
        assertTrue(peerHandlerSingleton.getPeers().get(0)[0].tablet.equals(session));
    }
}
