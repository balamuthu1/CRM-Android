package fr.pds.isintheair.crmtab;

import android.os.Build;



import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import fr.pds.isintheair.crmtab.controller.message.PhoningCampaignController;
import fr.pds.isintheair.crmtab.helper.PhoningCampaignHelper;
import fr.pds.isintheair.crmtab.model.entity.Contact;
import fr.pds.isintheair.crmtab.model.entity.ContactCampaign;
import fr.pds.isintheair.crmtab.model.entity.Customer;
import fr.pds.isintheair.crmtab.model.entity.HealthCenter;
import fr.pds.isintheair.crmtab.model.entity.Independant;
import fr.pds.isintheair.crmtab.model.entity.PhoningCampaign;
import fr.pds.isintheair.crmtab.view.fragment.CallPhoningCampaignFragment;
import retrofit.Retrofit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by tlacouque on 09/04/2016.
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
public class PhoningCampaignHelperTest {

    Contact contact;
    Contact contact2;
    Contact contact3;
    Contact contact4;
    Contact contact5;
    Customer indep;
    Customer hc;

    Contact contact6;
    ContactCampaign contactCampaign;

    List<ContactCampaign> contactCampaigns;
    PhoningCampaignController controller;
    HashMap<Customer,List<Contact>> customerListHashMap;
    LinkedHashMap<Customer,List<Contact>> listLinkedHashMap;

    @Before
    public void setUp() throws Exception {
        contact = new Contact();
        contact.setContactId(1);
        contact2 = new Contact();
        contact2.setContactId(2);
        contact3 = new Contact();
        contact3.setContactId(3);
        contact4 = new Contact();
        contact4.setContactId(4);
        contact5 = new Contact();
        contact5.setContactId(5);
        indep = new Independant();
        hc = new HealthCenter();

         contact6 = new Contact();
        contact6.setContactId(6);
         contactCampaign = new ContactCampaign();
        contactCampaign.setContactId(contact6.getContactId());

        List<Contact> listIndependant = new ArrayList<>();
        listIndependant.add(contact);
        listIndependant.add(contact4);
        List<Contact> listHealthCenter = new ArrayList<>();
        listHealthCenter.add(contact2);
        listHealthCenter.add(contact3);
        listHealthCenter.add(contact5);

        customerListHashMap = new HashMap<>();
        customerListHashMap.put(indep, listIndependant);
        customerListHashMap.put(hc, listHealthCenter);


        contactCampaigns = new ArrayList<>();
        for(Customer customer : customerListHashMap.keySet()) {
            for(Contact contact : customerListHashMap.get(customer) ) {
                ContactCampaign contactCampaign = new ContactCampaign();
                contactCampaign.setCampaignId(1);
                contactCampaign.setContactId(contact.getContactId());
                contactCampaign.setStatus(ContactCampaign.STATE_DEFINED);
                contactCampaigns.add(contactCampaign);
            }
        }

        listLinkedHashMap = new LinkedHashMap<>();
        listLinkedHashMap.put(indep,listIndependant);
        listLinkedHashMap.put(hc, listHealthCenter);

    }

    @Test
    public void testisLastContactOk() throws Exception {
        assertTrue(PhoningCampaignHelper.isLastContact(listLinkedHashMap,1,indep));
    }

    @Test
    public void testisLastContactNok() throws Exception {
        assertFalse(PhoningCampaignHelper.isLastContact(listLinkedHashMap, 0, hc));
    }

    @Test
    public void testContactCampaignInListYes() throws Exception {
        int size = contactCampaigns.size();
        ContactCampaign contactCampaign = new ContactCampaign(1,1,"info plus");
        PhoningCampaignHelper.contactCampaignInList(contactCampaign,contactCampaigns);
        assertTrue(size == contactCampaigns.size());
        assertTrue(contactCampaigns.contains(contactCampaign));
        int position = -1;
        for(int i = 0; i< contactCampaigns.size();i++) {
            if(contactCampaigns.get(i).getContactId() == contactCampaign.getContactId()) {
                position = i;
                break;
            }
        }
        assertTrue(contactCampaigns.get(position).getContactInfo().equals(contactCampaign.getContactInfo()));
    }
    @Test
    public void testContactCampaignInListNo() throws Exception {

        int size = contactCampaigns.size();
        PhoningCampaignHelper.contactCampaignInList(contactCampaign,contactCampaigns);
        assertTrue(size + 1 == contactCampaigns.size());
    }
}
