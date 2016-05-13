package fr.pds.isintheair.crmtab;

import android.os.Build;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.ResponseBody;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import fr.pds.isintheair.crmtab.controller.message.CallBackSavedResponseRestPhoningCampaign;
import fr.pds.isintheair.crmtab.controller.message.PhoningCampaignController;
import fr.pds.isintheair.crmtab.model.entity.Contact;
import fr.pds.isintheair.crmtab.model.entity.ContactCampaign;
import fr.pds.isintheair.crmtab.model.entity.Customer;
import fr.pds.isintheair.crmtab.model.entity.HealthCenter;
import fr.pds.isintheair.crmtab.model.entity.Independant;
import fr.pds.isintheair.crmtab.model.entity.PhoningCampaign;
import fr.pds.isintheair.crmtab.model.entity.ResponseRestPhoningCampaign;
import fr.pds.isintheair.crmtab.view.fragment.CallPhoningCampaignFragment;
import retrofit.Response;
import retrofit.Retrofit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * Created by tlacouque on 09/04/2016.
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
@PrepareForTest({Retrofit.class})
public class CallBackSavecResponseRestPhoningCampaignTest {

    Contact contact;
    Contact contact2;
    Contact contact3;
    Contact contact4;
    Contact contact5;
    Customer indep;
    Customer hc;
    CallPhoningCampaignFragment fragment;
    PhoningCampaign phoningCampaign;
    List<ContactCampaign> contactCampaigns;
    PhoningCampaignController controller;
    HashMap<Customer,List<Contact>> customerListHashMap;

    @Captor
    private ArgumentCaptor<CallBackSavedResponseRestPhoningCampaign> callbackArgumentCaptor;



    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
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
        fragment  = Mockito.mock(CallPhoningCampaignFragment.class);


        phoningCampaign = new PhoningCampaign();

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


        phoningCampaign.setStatut(PhoningCampaign.STATE_DEFINED);
        phoningCampaign.save();

        contactCampaigns = new ArrayList<>();
        for(Customer customer : customerListHashMap.keySet()) {
            for(Contact contact : customerListHashMap.get(customer) ) {
                ContactCampaign contactCampaign = new ContactCampaign();
                contactCampaign.setCampaignId(phoningCampaign.getCampaignId());
                contactCampaign.setContactId(contact.getContactId());
                contactCampaign.setStatus(ContactCampaign.STATE_DEFINED);
                contactCampaign.save();
                contactCampaigns.add(contactCampaign);
            }
        }

        controller = new PhoningCampaignController(customerListHashMap,phoningCampaign,fragment);
        LinkedHashMap<Customer,List<Contact>> listLinkedHashMap = new LinkedHashMap<>();
        listLinkedHashMap.put(indep,listIndependant);
        listLinkedHashMap.put(hc,listHealthCenter);
        controller.setCustomerListHashMap(listLinkedHashMap);


    }

    @Test
    public void testOnResponseValid() throws Exception {
        Mockito.doNothing().when(fragment).endCampaign(true);
        controller = spy(controller);
        CallBackSavedResponseRestPhoningCampaign cb = new CallBackSavedResponseRestPhoningCampaign(controller);
        controller.endRestCampaign(cb);
        controller.setFragment(fragment);
        ResponseRestPhoningCampaign response = new ResponseRestPhoningCampaign();
        response.setSaved(true);
        Response responseMocked = Response.success(response);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.github.com/").build();
        cb.onResponse(responseMocked, retrofit);

        assertTrue(controller.isBoolReponse());
        verify(fragment,Mockito.times(1)).endCampaign(true);

    }

    @Test
    public void testOnResponseBDDError() throws Exception {
        Mockito.doNothing().when(fragment).endCampaign(false);
        controller = spy(controller);
        CallBackSavedResponseRestPhoningCampaign cb = new CallBackSavedResponseRestPhoningCampaign(controller);
        controller.endRestCampaign(cb);
        controller.setFragment(fragment);
        ResponseRestPhoningCampaign response = new ResponseRestPhoningCampaign();
        response.setSaved(false);
        Response responseMocked = Response.success(response);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.github.com/").build();
        cb.onResponse(responseMocked, retrofit);
        assertFalse(controller.isBoolReponse());
        verify(fragment,Mockito.times(1)).endCampaign(false);
    }

    @Test
    public void testOnResponseServerRestError() throws Exception {
        Mockito.doNothing().when(fragment).endCampaign(false);
        controller = spy(controller);
        CallBackSavedResponseRestPhoningCampaign cb = new CallBackSavedResponseRestPhoningCampaign(controller);
        controller.endRestCampaign(cb);
        controller.setFragment(fragment);
        ResponseRestPhoningCampaign response = new ResponseRestPhoningCampaign();
        response.setSaved(false);
        ResponseBody responseBody = ResponseBody.create(MediaType.parse("500"),"test");
        Response responseMocked = Response.error(500, responseBody );
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.github.com/").build();
        cb.onResponse(responseMocked, retrofit);
        assertFalse(controller.isBoolReponse());
        verify(fragment,Mockito.times(1)).endCampaign(false);
    }

    @After
    public void tearDown() throws Exception {

        for(ContactCampaign contactCampaign : contactCampaigns) {
            contactCampaign.delete();
        }

        phoningCampaign.delete();
        FlowManager.destroy();

    }

}
