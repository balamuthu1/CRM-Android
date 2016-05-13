package fr.pds.isintheair.crmtab;

import android.os.Environment;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;

import fr.pds.isintheair.crmtab.helper.FormatValidator;
import fr.pds.isintheair.crmtab.model.entity.MapInfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created by tlacouque on 19/01/2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Environment.class, File.class}) // Prepare the static classes for mocking
 public class FormatValidatorTest {

    @Mock
    File file;

    @Test
    public void testFormatUrl() throws Exception {
        String urlWithoutHTTP = "www.url.com";
        String http           = "http://";
        assertEquals(http + urlWithoutHTTP, FormatValidator.formatUrl(urlWithoutHTTP));
        Assert.assertNotEquals(urlWithoutHTTP, FormatValidator.formatUrl(urlWithoutHTTP));
        assertEquals(http + urlWithoutHTTP, FormatValidator.formatUrl(http + urlWithoutHTTP));
    }

    @Test
    public void testisSiretSyntaxValide() throws Exception {
        final String siretNumberValid    = "26770008600056";
        final String siretNumberNotValid = "2677000860005";
        assertTrue(FormatValidator.isSiretSyntaxValide(siretNumberValid));
        Assert.assertFalse(FormatValidator.isSiretSyntaxValide(siretNumberNotValid));

    }

    @Test
    public void testformatPathTile() throws Exception {
        MapInfo mapInfo = Mockito.mock(MapInfo.class);
        when(mapInfo.getX()).thenReturn(15);
        when(mapInfo.getY()).thenReturn(16595);
        when(mapInfo.getZ()).thenReturn(11271);

        // Setup mocking for Environment and File classes
               mockStatic(Environment.class,File.class);


              // Make the Environment class return a mocked external storage directory
             when(Environment.getExternalStorageDirectory())
                       .thenReturn(file);
            when(file.toString()).thenReturn("");



        String string = FormatValidator.formatPathTile(mapInfo);
        assertEquals("/osmdroid/tiles/Mapnik/15/16595/11271.png.tile",string);

    }


    @Test
    public void testFormatUrlPathTile() throws Exception {
        MapInfo mapInfo = Mockito.mock(MapInfo.class);
        when(mapInfo.getX()).thenReturn(15);
        when(mapInfo.getY()).thenReturn(1212);
        when(mapInfo.getZ()).thenReturn(888);
        String returnString = FormatValidator.formatUrlPathTile(mapInfo);
        assertEquals(returnString,"/15/1212/888.png");
    }

    @Test
    public void testFormatUrlTile() throws Exception {
        MapInfo mapInfo = Mockito.mock(MapInfo.class);
        when(mapInfo.getX()).thenReturn(15);
        when(mapInfo.getY()).thenReturn(1212);
        when(mapInfo.getZ()).thenReturn(888);
        String returnString = FormatValidator.formatUrlTile(mapInfo);
        assertEquals(returnString,"/image/15/1212/888.png");

    }
}
