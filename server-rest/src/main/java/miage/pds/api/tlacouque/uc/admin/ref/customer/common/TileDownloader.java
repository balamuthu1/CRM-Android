package miage.pds.api.tlacouque.uc.admin.ref.customer.common;

import miage.pds.api.tlacouque.uc.admin.ref.customer.entities.MapInfo;

/**
 * Created by tlacouque on 01/02/2016.
 * Class used to start the tileDownloaderThread
 */
public class TileDownloader {


    /**
     * Method used to start a tileDownloader thread and return true if the tile was saved/downloaded.
     * @param mapInfo
     * @return boolean, return true if the tile was downloaded and saved, false if not.
     */
    public static boolean dwdTile(MapInfo mapInfo) {
        TileDownloaderThread tileDownloaderThread = new TileDownloaderThread(mapInfo);
        Thread threadDownloader = new Thread(tileDownloaderThread);
        threadDownloader.run();
        try {
            threadDownloader.join();
        } catch (InterruptedException e) {
            return false;
        }
        return tileDownloaderThread.isSaveDone();
    }


}
