package fr.pds.isintheair.notifier.controller;

import fr.pds.isintheair.notifier.entity.CallMessage;
import fr.pds.isintheair.notifier.entity.MessageType;

import javax.websocket.Session;

/******************************************
 * Created by        : jdatour            *
 * Creation date     : 03/19/16           *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

public class CallMessageController {
    public static void handleMessage (CallMessage callMessage, Session session) {
        MessageType messageType = callMessage.getMessageInfo().getMessageType();

        if (messageType.equals(MessageType.REGISTER)) {
            PeerHandlerSingleton.getInstance().addPeer(session, callMessage.getSessionInfo());
        }

        else {
            Session peerSession = PeerHandlerSingleton.getInstance().findPeerSession(callMessage.getSessionInfo());

            switch (callMessage.getMessageInfo().getMessageType()) {
                case CALL:
                    CallController.call(peerSession, callMessage.getCall().getPhoneNumber());
                    break;
                case CALL_ENDED:
                    CallController.endCall(peerSession);
                    break;
                case CALL_HOOKED:
                    CallController.notifyCallHooked(peerSession);
                    break;
                case CALL_PASSED:
                    CallController.notifyCallPassed(peerSession, callMessage.getCall().getPhoneNumber());
                    break;
                case CALL_RECEIVED:
                    CallController.notifyCallReceived(peerSession, callMessage.getCall().getPhoneNumber());
                    break;
            }
        }
    }
}
