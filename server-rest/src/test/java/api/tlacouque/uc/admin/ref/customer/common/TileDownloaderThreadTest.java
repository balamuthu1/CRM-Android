package api.tlacouque.uc.admin.ref.customer.common;

import miage.pds.api.tlacouque.uc.admin.ref.customer.common.TileDownloaderThread;
import miage.pds.api.tlacouque.uc.admin.ref.customer.entities.MapInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.util.FileSystemUtils;

import java.io.File;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by tlacouque on 28/02/2016.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest( { System.class})
public class TileDownloaderThreadTest {

    String catalinaBaseUrl;


    String catalinaUrl = System.getProperty("user.home");


    private static String URL_BASE = "http://tile.openstreetmap.org/";
    File file;
    MapInfo mapInfo;

    @Before
    public void setUp() throws Exception {
        System.setProperty("catalina.base",catalinaUrl);
        mapInfo = PowerMockito.mock(MapInfo.class);
        when(mapInfo.getX()).thenReturn(15);
        when(mapInfo.getY()).thenReturn(16597);
        when(mapInfo.getZ()).thenReturn(11270);
    }

    @Test
    public void testsaveImage() throws Exception {
        TileDownloaderThread tdt = new TileDownloaderThread(mapInfo);
        String url = TileDownloaderThread.formatUrl(mapInfo);
        String imageUrl = URL_BASE+url;
        String destinationFile = System.getProperty("catalina.base")+"/webapps/image/"+url;
        new File(destinationFile).getParentFile().mkdirs();
        tdt.saveImage(imageUrl,destinationFile);
        file = new File(catalinaUrl+"/webapps/image/" +TileDownloaderThread.formatUrl(mapInfo));
        assertTrue(file.exists());
    }

    @Test
    public void testRunOk() throws Exception {
        TileDownloaderThread tdt = new TileDownloaderThread(mapInfo);
        tdt.run();
        file = new File(catalinaUrl+"/webapps/image/" +TileDownloaderThread.formatUrl(mapInfo));
        assertTrue(file.exists());
    }

    @Test
    public void testRunNOk() throws Exception {
        when(mapInfo.getX()).thenReturn(32);
        TileDownloaderThread tdt = new TileDownloaderThread(mapInfo);
        tdt.run();
        file = new File(catalinaUrl+"/webapps/image/" +TileDownloaderThread.formatUrl(mapInfo));
        assertFalse(file.exists());
    }

    @Test
    public void testFormatUrl() throws Exception {
        assertEquals(TileDownloaderThread.formatUrl(mapInfo),"15/16597/11270.png");
    }

    @After
    public void tearDown() throws Exception {
        File index = new File(catalinaUrl+"/webapps");
        FileSystemUtils.deleteRecursively(index);
        System.setProperty("catalina.base","null");

    }
}
