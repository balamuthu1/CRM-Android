package fr.pds.isintheair.crmtab;

import android.os.Build;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.ResponseBody;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import fr.pds.isintheair.crmtab.controller.message.CallBackGetContactsForPhoningCampaign;
import fr.pds.isintheair.crmtab.model.entity.Contact;
import fr.pds.isintheair.crmtab.model.entity.ResponseRestPhoningCampaign;
import fr.pds.isintheair.crmtab.view.fragment.CreatePhoningCampaignFragment;
import retrofit.Response;
import retrofit.Retrofit;

import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.spy;

/**
 * Created by tlacouque on 26/04/2016.
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
@PrepareForTest({Retrofit.class})
public class CallBackGetContactsForPhoningCampaignTest {

    CreatePhoningCampaignFragment fragment;
    Contact contact1;
    Contact contact2;
    Contact contact3;
    Contact contact4;

    ArrayList<Contact> contacts;

    @Before
    public void setUp() throws Exception {
         contact1  = new Contact();
         contact2  = new Contact();
         contact3  = new Contact();
         contact4  = new Contact();
        contacts = new ArrayList<>();
        contacts.add(contact1);
        contacts.add(contact2);
        contacts.add(contact3);
        contacts.add(contact4);

        fragment  = Mockito.mock(CreatePhoningCampaignFragment.class);

    }

    @Test
    public void testOnResponseOk() throws Exception {
        Mockito.doNothing().when(fragment).restartCampaign(Mockito.anyList());
        CallBackGetContactsForPhoningCampaign cb = new CallBackGetContactsForPhoningCampaign(fragment);

        ResponseRestPhoningCampaign response = new ResponseRestPhoningCampaign();
        response.setContacts(contacts);
        Response responseMocked = Response.success(response);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.github.com/").build();
        cb.onResponse(responseMocked, retrofit);
        verify(fragment,Mockito.times(1)).restartCampaign(contacts);
    }

    @Test
    public void testOnResponseNok() throws Exception {
        Mockito.doNothing().when(fragment).restartCampaign(Mockito.anyList());
        CallBackGetContactsForPhoningCampaign cb = new CallBackGetContactsForPhoningCampaign(fragment);
        cb = spy(cb);
        Mockito.doNothing().when(cb).onFailure(null);
        ResponseRestPhoningCampaign response = new ResponseRestPhoningCampaign();
        ResponseBody responseBody = ResponseBody.create(MediaType.parse("500"),"test");
        Response responseMocked = Response.error(500, responseBody );
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.github.com/").build();
        cb.onResponse(responseMocked, retrofit);
        verify(cb,Mockito.times(1)).onFailure(null);
    }

    @Test
    public void testOnFailure() throws Exception {
        Mockito.doNothing().when(fragment).callRestFailed();
        CallBackGetContactsForPhoningCampaign cb = new CallBackGetContactsForPhoningCampaign(fragment);

        ResponseRestPhoningCampaign response = new ResponseRestPhoningCampaign();
        response.setContacts(contacts);
        cb.onFailure(null);
        verify(fragment, Mockito.times(1)).callRestFailed();
    }
}
