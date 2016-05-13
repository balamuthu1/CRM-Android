package fr.pds.isintheair.notifier.server;

import fr.pds.isintheair.notifier.controller.CallMessageController;
import fr.pds.isintheair.notifier.controller.PeerHandlerSingleton;
import fr.pds.isintheair.notifier.entity.CallMessage;
import fr.pds.isintheair.notifier.helper.JSONHelper;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.logging.Logger;

/******************************************
 * Created by        : jdatour            *
 * Creation date     : 03/19/16           *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

@ServerEndpoint("/call")
public class CallNotifierEndpoint {
    private final static Logger logger = Logger.getLogger(CallNotifierEndpoint.class.getName());

    @OnOpen
    public void onOpen (Session session) {
        logger.info("Call session opened");
    }

    @OnError
    public void onError (Session session, Throwable throwable) {
        logger.info("Call error : " + throwable.getMessage());
    }

    @OnMessage
    public void onMessage (String body, Session session) {
        logger.info("Call message received : " + body);

        CallMessage callMessage = (CallMessage) JSONHelper.deserialize(body, CallMessage.class);

        CallMessageController.handleMessage(callMessage, session);
    }

    @OnClose
    public void onClose (Session session, CloseReason closeReason) {
        logger.info("Call closed session : " + closeReason.getReasonPhrase());

        PeerHandlerSingleton.getInstance().removePeerSession(session);
    }
}
