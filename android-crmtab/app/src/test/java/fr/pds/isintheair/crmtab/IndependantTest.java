package fr.pds.isintheair.crmtab;

import android.os.Build;

import com.raizlabs.android.dbflow.config.FlowManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import fr.pds.isintheair.crmtab.model.entity.Company;
import fr.pds.isintheair.crmtab.model.entity.Independant;
import fr.pds.isintheair.crmtab.model.entity.Specialty;

import static org.junit.Assert.assertEquals;

/**
 * Created by tlacouque on 20/01/2016.
 */

@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
public class IndependantTest {
    Independant independant;
    Specialty   specialtyRadiologue;
    Specialty   specialtyAucune;
    Company     companyAucune;
    Company     companyMSPDeDenfert;

    @Before
    public void setUp() throws Exception {
        initDb();
        independant = new Independant();
    }

    @Test
    public void testGetPurchasingCentral() throws Exception {
        independant.setCompanyId(companyMSPDeDenfert.getId());
        assertEquals(companyMSPDeDenfert.getId(), independant.getCompany().getId());
    }

    @Test
    public void testGetHolding() throws Exception {
        independant.setSpecialtyId(specialtyRadiologue.getId());
        assertEquals(specialtyRadiologue.getId(), independant.getSpecialty().getId());
    }


    @After
    public void tearDown() throws Exception {
        cleanDb();
        FlowManager.destroy();
    }


    public void initDb() {
        specialtyAucune = new Specialty();
        specialtyAucune.setName("Aucune");
        specialtyAucune.setId(0);
        specialtyAucune.save();

        specialtyRadiologue = new Specialty();
        specialtyRadiologue.setName("Radiologue");
        specialtyRadiologue.setId(1);
        specialtyRadiologue.save();

        companyAucune = new Company();
        companyAucune.setName("Aucune");
        companyAucune.setId(0);
        companyAucune.save();

        companyMSPDeDenfert = new Company();
        companyMSPDeDenfert.setName("MDP de Denfert");
        companyMSPDeDenfert.setId(1);
        companyMSPDeDenfert.save();

    }


    public void cleanDb() {
        specialtyAucune.delete();
        specialtyRadiologue.delete();
        companyAucune.delete();
        companyMSPDeDenfert.delete();
    }


}
