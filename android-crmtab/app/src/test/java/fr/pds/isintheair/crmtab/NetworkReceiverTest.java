package fr.pds.isintheair.crmtab;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import com.raizlabs.android.dbflow.config.FlowManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.internal.ShadowExtractor;
import org.robolectric.shadows.ShadowConnectivityManager;
import org.robolectric.shadows.ShadowNetworkInfo;

import fr.pds.isintheair.crmtab.BuildConfig;
import fr.pds.isintheair.crmtab.view.activity.MainActivity;
import fr.pds.isintheair.crmtab.controller.broadcastreceiver.NetworkReceiver;
import fr.pds.isintheair.crmtab.view.fragment.DetailIndepFragment;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Created by tlacouque on 05/03/2016.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP,
        manifest = "src/main/AndroidManifest.xml")
public class NetworkReceiverTest {

    ConnectivityManager cm;
    ShadowConnectivityManager shadowCM;
    ShadowNetworkInfo shadowOfActiveNetworkInfo;
    DetailIndepFragment indepFragment; NetworkReceiver networkReceiver; MainActivity mainActivity;

    @Before
    public void setUp() throws Exception {
        indepFragment = Mockito.mock(DetailIndepFragment.class);
         networkReceiver = new NetworkReceiver(indepFragment);
         mainActivity = new MainActivity();
        cm = (ConnectivityManager)
                RuntimeEnvironment.application.getSystemService(Context.CONNECTIVITY_SERVICE);
        shadowCM = (ShadowConnectivityManager) ShadowExtractor.extract(cm);
        shadowOfActiveNetworkInfo = (ShadowNetworkInfo) ShadowExtractor.extract(shadowCM.getActiveNetworkInfo());

    }

    @Test
    public void testOnReceiveInternetAvailable() throws Exception {
        NetworkInfo networkInfo =
                ShadowNetworkInfo.newInstance
                        (NetworkInfo.DetailedState.CONNECTED, ConnectivityManager.TYPE_WIFI, 0, true, true);
        shadowCM.setActiveNetworkInfo(networkInfo);
        Intent intent = Mockito.mock(Intent.class);
        networkReceiver.onReceive(mainActivity.getApplicationContext(),intent);
        assertTrue(networkReceiver.networkAvailable);
    }

    @Test
    public void testOnReceiveInternetNotAvailable() throws Exception {
        NetworkInfo networkInfo =
                ShadowNetworkInfo.newInstance
                        (NetworkInfo.DetailedState.DISCONNECTED, ConnectivityManager.TYPE_WIFI, 0, false, false);
        shadowCM.setActiveNetworkInfo(networkInfo);
        Intent intent = Mockito.mock(Intent.class);
        networkReceiver.onReceive(mainActivity.getApplicationContext(),intent);
        assertFalse(networkReceiver.networkAvailable);
    }

    @After
    public void tearDown() throws Exception {
        FlowManager.destroy();
    }


}
