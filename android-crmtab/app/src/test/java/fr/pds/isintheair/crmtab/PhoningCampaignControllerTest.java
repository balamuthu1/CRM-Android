package fr.pds.isintheair.crmtab;

import android.os.Build;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.raizlabs.android.dbflow.config.FlowManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import fr.pds.isintheair.crmtab.model.entity.MessageRestPhoningCampaign;
import fr.pds.isintheair.crmtab.model.entity.PhoningCampaign;
import fr.pds.isintheair.crmtab.model.entity.ResponseRestPhoningCampaign;
import fr.pds.isintheair.crmtab.view.fragment.CallPhoningCampaignFragment;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static retrofit.Retrofit.*;

/**
 * Created by tlacouque on 07/04/2016.
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
@PrepareForTest({Retrofit.class})
public class PhoningCampaignControllerTest {
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
    public void testEndCallPassNextContact() throws Exception {
        controller.setCurrentCustomer(indep);
        controller = spy(controller);
        Mockito.doNothing().when(controller).beginCall();
        controller.endCall();
        verify(controller,Mockito.times(1)).beginCall();
        verify(controller,Mockito.times(1)).updateCurrentCustomer();
        assertEquals(1,controller.getCurrentContactPosition());
    }

    @Test
    public void testEndCallPassNextCustomer() throws Exception {
        controller.setCurrentCustomer(indep);
        controller.setCurrentContactPosition(1);
        controller = spy(controller);
        Mockito.doNothing().when(controller).beginCall();
        controller.endCall();
        verify(controller, Mockito.times(1)).beginCall();
        verify(controller,Mockito.times(1)).updateCurrentCustomer();
        assertEquals(hc, controller.getCurrentCustomer());
    }

    @Test
    public void testEndCallEndCampaign() throws Exception {

        controller.setCurrentCustomer(hc);
        controller.setCurrentContactPosition(2);
        controller = spy(controller);
        Mockito.doNothing().when(controller).endCampaign();
        controller.endCall();
        verify(controller, Mockito.times(1)).endCampaign();
    }

    @Test
    public void testUserCalledContactAfter() throws Exception {

        LinkedHashMap<Customer,List<Contact>> customerListReseted = new LinkedHashMap<>();
        ArrayList<Contact> contacts = new ArrayList<>();
        contacts.add(contact4);
        customerListReseted.put(indep, contacts);
        controller.setCustomerListReseted(customerListReseted);

        controller.setCurrentCustomer(hc);
        controller.setCurrentContactPosition(2);
        controller = spy(controller);
        Mockito.doNothing().when(controller).beginCall();
        controller.endCall();
        verify(controller, Mockito.times(1)).beginCall();
        verify(controller,Mockito.times(1)).updateCurrentCustomer();
        assertEquals(indep, controller.getCurrentCustomer());
        assertEquals(0, controller.getCurrentContactPosition());
        assertEquals(0, controller.getCurrentCustomerposition());
    }


    @Test
    public void testBeginCall() throws Exception {

        Mockito.doNothing().when(fragment)
                .initView(any(PhoningCampaign.class), any(Contact.class), any(Customer.class), any(ContactCampaign.class));
        Mockito.doNothing().when(fragment).startCall();
        controller.updateCurrentCustomer();
        controller.beginCall();
        assertEquals(controller.getContactCampaign().getContactId(), 1);
        assertEquals(controller.getCurrentContact(), contact);

    }

    @Test
    public void testBeginCampaign() throws Exception {
        controller = spy(controller);
        Mockito.doNothing().when(controller).beginCall();
        Mockito.doNothing().when(controller).updateCurrentCustomer();
        controller.beginCampaign();
        assertEquals(PhoningCampaign.STATE_BEGINED, phoningCampaign.getStatut());
        verify(controller, Mockito.times(1)).beginCall();
        verify(controller,Mockito.times(1)).updateCurrentCustomer();

    }

    @Test
    public void testUpdateCurrentCustomer() throws Exception {
        controller.setCurrentCustomerposition(1);
        controller.updateCurrentCustomer();
        assertEquals(hc, controller.getCurrentCustomer());
    }

    @Test
    public void testSaveCurrentContactInfo() throws Exception {
       ContactCampaign contactCampaign = contactCampaigns.get(0);
        String commentary = "commentary";
        controller.setContactCampaign(contactCampaign);
        controller.saveCurrentContactInfo(commentary, ContactCampaign.STATE_ENDED);
        assertEquals(commentary, contactCampaign.getContactInfo());
        assertEquals(ContactCampaign.STATE_ENDED,contactCampaign.getStatus());
    }

    @Test
    public void testEndCampaign() throws Exception {
        controller = spy(controller);
        Mockito.doNothing().when(controller).endRestCampaign(Mockito.any(CallBackSavedResponseRestPhoningCampaign.class));
        Date date = new Date();
        PowerMockito.whenNew(Date.class).withNoArguments().thenReturn(date);
        controller.endCampaign();
        assertEquals(PhoningCampaign.STATE_ENDED,phoningCampaign.getStatut());
    }

    @Test
    public void testResetCall() throws Exception {
        controller.setCurrentContact(contact2);
        controller = spy(controller);
        Mockito.doNothing().when(controller).endCall();
        controller.resetCall();
        assertEquals(contact2,controller.getResetContact().get(0));
    }

    @Test
    public void testEndRestCampaignValid() throws Exception {
        Mockito.doNothing().when(fragment).endCampaign(true);
        controller = spy(controller);
        CallBackSavedResponseRestPhoningCampaign cb = new CallBackSavedResponseRestPhoningCampaign(controller);
        controller.endRestCampaign(cb);
        controller.setFragment(fragment);
        ResponseRestPhoningCampaign response = new ResponseRestPhoningCampaign();
        response.setSaved(true);
        Response responseMocked = Response.success(response);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.github.com/").build();
        verify(controller).endRestCampaign(callbackArgumentCaptor.capture());
        callbackArgumentCaptor.getValue().onResponse(responseMocked, retrofit);

        assertTrue(controller.isBoolReponse());
    }

    @Test
    public void testEndRestCampaignError() throws Exception {
        Mockito.doNothing().when(fragment).endCampaign(false);
        controller = spy(controller);
        CallBackSavedResponseRestPhoningCampaign cb = new CallBackSavedResponseRestPhoningCampaign(controller);
        controller.endRestCampaign(cb);
        controller.setFragment(fragment);
        ResponseRestPhoningCampaign response = new ResponseRestPhoningCampaign();
        response.setSaved(true);
        Response responseMocked = Response.success(response);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.github.com/").build();
        verify(controller).endRestCampaign(callbackArgumentCaptor.capture());
        Throwable t = new Throwable("message");
        callbackArgumentCaptor.getValue().onFailure(t);

        assertFalse(controller.isBoolReponse());
    }

    @Test
    public void testPauseCampaign() throws Exception {
        controller.pauseCampaign();
        verify(fragment, Mockito.times(1)).stopCampaign();
        assertEquals(PhoningCampaign.STATE_STOPPED,phoningCampaign.getStatut());
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
