package fr.pds.isintheair.notifier.server;

import org.glassfish.tyrus.server.Server;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Properties;

/******************************************
 * Created by        : jdatour            *
 * Creation date     : 03/19/16           *
 * Modified by       :                    *
 * Modification date :                    *
 ******************************************/

public class NotifierServer {

    public static void main (String[] args) {
        try {
            runServer();
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void runServer () throws URISyntaxException, IOException {
        String      port        = null;
        Properties  properties  = new Properties();
        InputStream inputStream = null;

        try {
            inputStream = NotifierServer.class.getClassLoader().getResourceAsStream("configuration.properties");

            properties.load(inputStream);

            port = properties.getProperty("port");
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        Server server = new Server("localhost", Integer.parseInt(port), "/", ServerConfiguration.class);

        ((Runnable) () -> {
            try {
                server.start();
                Thread.currentThread().join();
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
            finally {
                server.stop();
            }
        }).run();
    }
}
