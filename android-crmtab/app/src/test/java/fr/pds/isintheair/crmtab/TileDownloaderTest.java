package fr.pds.isintheair.crmtab;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;

import com.raizlabs.android.dbflow.config.FlowManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.File;

import fr.pds.isintheair.crmtab.BuildConfig;
import fr.pds.isintheair.crmtab.R;
import fr.pds.isintheair.crmtab.view.activity.LoginActivity;
import fr.pds.isintheair.crmtab.model.asynctask.TileDownloader;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by tlacouque on 29/02/2016.
 */

@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
public class TileDownloaderTest {

    TileDownloader td;
    LoginActivity activity;

    @Before
    public void setUp() throws Exception {
     //   activity = Robolectric.setupActivity(LoginActivity.class);
         td = new TileDownloader();
    }

    @Test
    public void testSaveImageOk() throws Exception {

        String PATH = Environment.getExternalStorageDirectory().toString()+"/test.png";
        Bitmap bitmap = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.logo);
        td.saveImage(bitmap,PATH);
        File file = new File(PATH);
        assertTrue(file.exists());
        file.delete();
    }

    @Test
    public void testDownloadBitmapOk() throws Exception {
        String PATH = "http://a.tile.openstreetmap.org/15/16595/11271.png";
        Bitmap bitmap = td.downloadBitmap(PATH);
        assertNotNull(bitmap);
    }

    @Test
    public void testDownloadBitmapNOk() throws Exception {
        String PATH = "http://a.tile.openstreetmap.org/32/16595/11271.png";
        Bitmap bitmap = td.downloadBitmap(PATH);
        assertNull(bitmap);
    }

    @After
    public void tearDown() throws Exception {
        FlowManager.destroy();
    }
}
