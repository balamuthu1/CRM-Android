package fr.pds.isintheair.crmtab;

import android.accounts.Account;
import android.content.Intent;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;

import fr.pds.isintheair.crmtab.ctruong.uc.propsect.suggestion.view.activity.SynchronisationActivity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Created by Truong on 5/8/2016.
 */
public class SynchronisationActivityTest {

    @Before
    public void setUp() throws Exception {


    }


    @Test
    public void testStartForceSyncing() throws Exception {
        SynchronisationActivity activity = Mockito.mock(SynchronisationActivity.class);

    }


    @Test
    public void testScheduleSync() throws Exception {
        SynchronisationActivity activity = Mockito.mock(SynchronisationActivity.class);
    }

    @Test
    public void testCreateDemoAccount() throws Exception {
        SynchronisationActivity activity = Mockito.mock(SynchronisationActivity.class);
        Account account = activity.createDemoAccount();
        assertNull(account);
        assertNotEquals("Demo", account);
    }
}
