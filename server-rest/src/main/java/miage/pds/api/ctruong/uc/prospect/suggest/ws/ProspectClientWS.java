package miage.pds.api.ctruong.uc.prospect.suggest.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

/**
 * Created by Truong on 3/20/2016.
 */
@ClientEndpoint
public class ProspectClientWS {
    Session session = null;
    private MessageHandler messageHandler;
    private static final Logger log = LoggerFactory.getLogger(ProspectClientWS.class);

    public ProspectClientWS(URI uri) {
        try {
            WebSocketContainer webSocketContainer = ContainerProvider.getWebSocketContainer();
            webSocketContainer.connectToServer(this, uri);
        } catch (DeploymentException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session session){
        log.info("opening websocket");
        this.session = session;
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        log.info("closing websocket becuz: " + reason);
        this.session = null;
    }

    @OnMessage
    public void onMessage(String message) {
        if (this.messageHandler != null) {
            this.messageHandler.handleMessage(message);
        }
    }
    public void addMessageHandler(MessageHandler msgHandler) {
        this.messageHandler = msgHandler;
    }


    public void sendMessage(String message) {
        this.session.getAsyncRemote().sendText(message);
    }

    public static interface MessageHandler {

        public void handleMessage(String message);
    }
}
