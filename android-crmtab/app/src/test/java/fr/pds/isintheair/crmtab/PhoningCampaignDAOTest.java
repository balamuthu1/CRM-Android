package fr.pds.isintheair.crmtab;

import android.os.Build;

import com.raizlabs.android.dbflow.config.FlowManager;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import fr.pds.isintheair.crmtab.model.dao.PhoningCampaignDAO;
import fr.pds.isintheair.crmtab.model.entity.PhoningCampaign;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by tlacouque on 12/04/2016.
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
public class PhoningCampaignDAOTest {

    @Test
    public void testGetStoppedPhoningCampaignNotNull() throws Exception {
        String theme = "Theme";
        PhoningCampaign phoningCampaign = new PhoningCampaign();
        phoningCampaign.setCampaignTheme(theme);
        phoningCampaign.setStatut(PhoningCampaign.STATE_STOPPED);
        phoningCampaign.save();
        PhoningCampaign ph = PhoningCampaignDAO.getStoppedPhoningCampaign();
        phoningCampaign.delete();
        assertEquals(ph.getCampaignTheme(), phoningCampaign.getCampaignTheme());
    }

    @Test
    public void testGetStoppedPhoningCampaignNull() throws Exception {
        PhoningCampaign ph = PhoningCampaignDAO.getStoppedPhoningCampaign();
        assertNull(ph);
    }

    @After
    public void tearDown() throws Exception {
        FlowManager.destroy();
    }
}
