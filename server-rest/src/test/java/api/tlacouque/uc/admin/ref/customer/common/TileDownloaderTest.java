package api.tlacouque.uc.admin.ref.customer.common;

import miage.pds.api.tlacouque.uc.admin.ref.customer.common.TileDownloader;
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by tlacouque on 02/02/2016.
 */
 @RunWith(PowerMockRunner.class)
 @PrepareForTest( { System.class })
 public class TileDownloaderTest {

    String catalinaBaseUrl;

    String catalinaUrl = System.getProperty("user.home");

    File file;

    @Before
    public void setUp() throws Exception {
        catalinaBaseUrl = System.getProperty("catalina.base");
        System.setProperty("catalina.base",catalinaUrl);

    }

    @Test
    public void testdwdTileOk() throws Exception {
       PowerMockito.mockStatic(System.class);
        when(System.getProperty("catalina.base")).thenReturn(catalinaUrl);


        MapInfo mapInfo = PowerMockito.mock(MapInfo.class);
        when(mapInfo.getX()).thenReturn(15);
        when(mapInfo.getY()).thenReturn(16597);
        when(mapInfo.getZ()).thenReturn(11270);
        TileDownloader.dwdTile(mapInfo);
        file = new File(catalinaUrl+"/webapps/image/" +TileDownloaderThread.formatUrl(mapInfo));
        assertTrue(file.exists());
    }

    @Test
    public void testdwdTileTileNotFound() throws Exception {
        PowerMockito.mockStatic(System.class);
        when(System.getProperty("catalina.base")).thenReturn(catalinaUrl);


        MapInfo mapInfo = PowerMockito.mock(MapInfo.class);
        when(mapInfo.getX()).thenReturn(32);
        when(mapInfo.getY()).thenReturn(16598);
        when(mapInfo.getZ()).thenReturn(11271);
        TileDownloader.dwdTile(mapInfo);
        file = new File(catalinaUrl+"/webapps/image/" +TileDownloaderThread.formatUrl(mapInfo));
        assertFalse(file.exists());
    }



    @After
    public void tearDown() throws Exception {
        File index = new File(catalinaUrl+"/webapps");
        FileSystemUtils.deleteRecursively(index);
        System.setProperty("catalina.base",catalinaBaseUrl);
    }
}
