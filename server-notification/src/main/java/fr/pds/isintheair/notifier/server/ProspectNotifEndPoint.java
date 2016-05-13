package fr.pds.isintheair.notifier.server;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * The EndPoint to connect to the websocket server
 * Created by Truong on 1/23/2016.
 *
 * @version 1.0.0
 * @since 2016-01-24
 */
@ServerEndpoint("/prospect")
public class ProspectNotifEndPoint {

    static final Logger log = Logger.getLogger(ProspectNotifEndPoint.class.getSimpleName());


    @OnOpen
    public void onOpen(Session session) {
        log.info("I'm starting");
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        log.info(message);
        session.getBasicRemote().sendText(message);
    }

    @OnClose
    public void onClose (Session session) {
        log.info("I'm closing ...");
    }


}
