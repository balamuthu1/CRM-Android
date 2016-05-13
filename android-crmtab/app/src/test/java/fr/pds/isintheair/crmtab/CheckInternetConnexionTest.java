package fr.pds.isintheair.crmtab;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import com.raizlabs.android.dbflow.config.FlowManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.internal.ShadowExtractor;
import org.robolectric.shadows.ShadowConnectivityManager;
import org.robolectric.shadows.ShadowNetworkInfo;

import fr.pds.isintheair.crmtab.BuildConfig;
import fr.pds.isintheair.crmtab.view.activity.CRUDCustomerActivity;
import fr.pds.isintheair.crmtab.helper.CheckInternetConnexion;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by tlacouque on 19/01/2016.
 */

@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP,
manifest = "src/main/AndroidManifest.xml")
@RunWith(RobolectricGradleTestRunner.class)
public class CheckInternetConnexionTest {
    CRUDCustomerActivity activity;
    ConnectivityManager cm;
    ShadowConnectivityManager shadowCM;
    ShadowNetworkInfo shadowOfActiveNetworkInfo;

    @Before
    public void setUp() throws Exception {
        //activity = Robolectric.setupActivity(CRUDCustomerActivity.class);
        // cm = (ConnectivityManager) RuntimeEnvironment.application.getSystemService(Context.CONNECTIVITY_SERVICE);
        cm = (ConnectivityManager)
                 RuntimeEnvironment.application.getSystemService(Context.CONNECTIVITY_SERVICE);

        shadowCM = (ShadowConnectivityManager) ShadowExtractor.extract(cm);
        // shadowCM = Shadows.shadowOf(cm);
        shadowOfActiveNetworkInfo = (ShadowNetworkInfo) ShadowExtractor.extract(shadowCM.getActiveNetworkInfo());
    }

    @Test
    public void testNoInternetConnexionAvailable() throws Exception {

        NetworkInfo networkInfo =
                ShadowNetworkInfo.newInstance
                        (NetworkInfo.DetailedState.DISCONNECTED, ConnectivityManager.TYPE_WIFI, 0, false, false);

       shadowCM.setActiveNetworkInfo(networkInfo);



        assertFalse(CheckInternetConnexion.isNetworkAvailable(RuntimeEnvironment.application));
    }

    @Test
    public void testInternetConnexionAvailable() throws Exception {

        NetworkInfo networkInfo =
                ShadowNetworkInfo.newInstance
                        (NetworkInfo.DetailedState.CONNECTED, ConnectivityManager.TYPE_WIFI, 0, true, true);

        shadowCM.setActiveNetworkInfo(networkInfo);

        assertTrue(CheckInternetConnexion.isNetworkAvailable(RuntimeEnvironment.application));
    }


    @After
    public void tearDown() throws Exception {
        FlowManager.destroy();
    }


}
