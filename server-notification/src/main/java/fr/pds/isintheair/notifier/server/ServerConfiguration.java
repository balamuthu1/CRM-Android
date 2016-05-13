package fr.pds.isintheair.notifier.server;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/******************************************
 * Created by        : jdatour            *
 * Creation date     : 03/19/16           *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

public class ServerConfiguration implements ServerApplicationConfig {
    @Override
    public Set<ServerEndpointConfig> getEndpointConfigs (Set<Class<? extends Endpoint>> set) {
        return Collections.emptySet();
    }

    @Override
    public Set<Class<?>> getAnnotatedEndpointClasses (Set<Class<?>> set) {
        return new HashSet<Class<?>>() {
            {
                add(CallNotifierEndpoint.class);
                add(CalendarNotifierEndpoint.class);
                add(ProspectNotifEndPoint.class);
            }
        };
    }
}
