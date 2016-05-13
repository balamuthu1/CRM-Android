package fr.pds.isintheair.notifier.controller;

import fr.pds.isintheair.notifier.entity.NotificationType;
import fr.pds.isintheair.notifier.entity.Peer;
import fr.pds.isintheair.notifier.entity.SessionInfo;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/******************************************
 * Created by        : jdatour            *
 * Creation date     : 03/19/16           *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

public class PeerHandlerSingleton {
    private static final Integer              CALL_PEER_INDEX     = 0;
    private static final Integer              CALENDAR_PEER_INDEX = 1;
    private static       PeerHandlerSingleton INSTANCE            = null;

    private HashMap<Integer, Peer<Session, Session>[]> peers;

    private PeerHandlerSingleton () {
        peers = new HashMap<>();
    }

    public static synchronized PeerHandlerSingleton getInstance () {
        if (INSTANCE == null) {
            INSTANCE = new PeerHandlerSingleton();
        }

        return INSTANCE;
    }

    public HashMap<Integer, Peer<Session, Session>[]> getPeers () {
        return peers;
    }

    public synchronized void addPeer (Session session, SessionInfo sessionInfo) {
        Peer<Session, Session>[] sessionPeers = peers.get(sessionInfo.getUserId());
        Peer<Session, Session>   peerType     = null;

        if (sessionPeers == null) {
            sessionPeers = new Peer[2];
            peerType = new Peer<>();

            peers.put(sessionInfo.getUserId(), sessionPeers);
        }

        else {
            peerType = getPeer(sessionInfo.getNotificationType(), sessionPeers);

            if (peerType == null)
                peerType = new Peer<>();
        }

        switch (sessionInfo.getDeviceType()) {
            case PHONE:
                peerType.phone = session;
                break;
            case TABLET:
                peerType.tablet = session;
                break;
        }

        switch (sessionInfo.getNotificationType()) {
            case CALENDAR:
                sessionPeers[CALENDAR_PEER_INDEX] = peerType;
                break;
            case CALL:
                sessionPeers[CALL_PEER_INDEX] = peerType;
                break;
        }
    }

    public synchronized Session findPeerSession (SessionInfo sessionInfo) {
        Peer<Session, Session>[] sessionPeers = peers.get(sessionInfo.getUserId());
        Peer<Session, Session>   peerType     = getPeer(sessionInfo.getNotificationType(), sessionPeers);

        if (peerType != null) {
            switch (sessionInfo.getDeviceType()) {
                case PHONE:
                    return peerType.tablet;
                case TABLET:
                    return peerType.phone;
            }
        }

        return null;
    }

    public synchronized void removePeerSession (Session session) {
        Iterator it    = peers.entrySet().iterator();
        Boolean  found = false;

        while (it.hasNext()) {
            Map.Entry                pair         = (Map.Entry) it.next();
            Peer<Session, Session>[] sessionPeers = peers.get(pair.getKey());

            for (int i = 0; i < sessionPeers.length; ++i) {
                Peer<Session, Session> peer = sessionPeers[i];

                if (peer.tablet == session) {
                    peer.tablet = null;
                    found = true;
                }

                if (peer.phone == session) {
                    peer.phone = null;
                    found = true;
                }

                if (found)
                    break;
            }

            if (found)
                break;

            it.remove();
        }
    }

    private Peer<Session, Session> getPeer (NotificationType notificationType, Peer<Session, Session>[] sessionPeers) {
        switch (notificationType) {
            case CALENDAR:
                return sessionPeers[CALENDAR_PEER_INDEX];
            case CALL:
                return sessionPeers[CALL_PEER_INDEX];
        }

        return null;
    }
}
