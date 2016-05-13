package miage.pds.api.tlacouque.uc.admin.ref.customer.common;

import miage.pds.api.tlacouque.uc.admin.ref.customer.entities.MapInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;

/**
 * Created by tlacouque on 03/02/2016.
 * Thread used to download the tile from the mapinfo
 */
public class TileDownloaderThread implements Runnable  {

    MapInfo mapInfo;
    public static String URL_BASE = "http://tile.openstreetmap.org/";
    private boolean saveDone;
    private static final Logger logger = LoggerFactory.getLogger(TileDownloaderThread.class);

    public TileDownloaderThread(MapInfo mapInfo) {
        this.mapInfo = mapInfo;
    }

    @Override
    public void run() {
        String url = formatUrl(mapInfo);
        String imageUrl = URL_BASE+url;
        String destinationFile = System.getProperty("catalina.base")+"/webapps/image/"+url;
        new File(destinationFile).getParentFile().mkdirs();

        try {
            saveImage(imageUrl, destinationFile);
            saveDone = true;
        } catch (IOException e) {
            saveDone = false;
        }

    }

    public boolean isSaveDone() {
        return saveDone;
    }

    /**
     * Download/save the image on the tomcat directory
     * @param imageUrl
     * @param destinationFile
     * @throws IOException
     */
    public void saveImage(String imageUrl, String destinationFile) throws IOException {
        URL url = new URL(imageUrl);
       // Proxy proxy = new Proxy(Proxy.Type.HTTP,new InetSocketAddress("proxy.inside.esiag.info",3128));
       // InputStream is = url.openConnection(proxy).getInputStream();
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(destinationFile);

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }

        is.close();
        os.close();
    }

    /**
     * Format the url to save it on the right directory
     * @param mapInfo
     * @return
     */
    public static String formatUrl(MapInfo mapInfo) {
        return mapInfo.getX()+ "/" + mapInfo.getY()+"/"+mapInfo.getZ()+".png";
    }
}
