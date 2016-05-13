package fr.pds.isintheair.crmtab;

import android.os.Build;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.ResponseBody;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import fr.pds.isintheair.crmtab.controller.message.CallBackGetContactsForPhoningCampaign;
import fr.pds.isintheair.crmtab.controller.message.CallBackGetOtherCustomers;
import fr.pds.isintheair.crmtab.model.entity.Customer;
import fr.pds.isintheair.crmtab.model.entity.HealthCenter;
import fr.pds.isintheair.crmtab.model.entity.Independant;
import fr.pds.isintheair.crmtab.model.entity.ResponseRestCustomer;
import fr.pds.isintheair.crmtab.model.entity.ResponseRestPhoningCampaign;
import fr.pds.isintheair.crmtab.view.fragment.CreatePhoningCampaignFragment;
import retrofit.Response;
import retrofit.Retrofit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.spy;

/**
 * Created by tlacouque on 26/04/2016.
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
@PrepareForTest({Retrofit.class})
public class CallBackGetOtherCustomersTest {

    CreatePhoningCampaignFragment fragment;
    List<HealthCenter> list;
    List<Customer> customersPassed;
    List<Independant> independantReturned;
    List<HealthCenter> healthCenterReturned;
    HealthCenter hc1;
    HealthCenter hc2;
    HealthCenter hc3;
    HealthCenter hc4;
    HealthCenter hcReturned1;
    HealthCenter hcReturned2;

    Independant customerPass1;
    Independant customerPass2;

    Independant customerReturn1;
    Independant customerReturn2;
    Independant customerReturn3;


    @Before
    public void setUp() throws Exception {
        fragment  = Mockito.mock(CreatePhoningCampaignFragment.class);
        list = new ArrayList<>();
        customersPassed = new ArrayList<>();
        independantReturned = new ArrayList<>();
        healthCenterReturned = new ArrayList<>();
        hc1 = new HealthCenter();
        hc2 = new HealthCenter();
        hc3 = new HealthCenter();
        hc4 = new HealthCenter();
        hcReturned1 = new HealthCenter();
        hcReturned2 = new HealthCenter();
        customerPass1 = new Independant();
        customerPass2 = new Independant();
        customerReturn1 = new Independant();
        customerReturn2 = new Independant();
        customerReturn3 = new Independant();

        list.add(hc1);
        list.add(hc2);
        list.add(hc3);
        list.add(hc4);

        customersPassed.add(customerPass1);
        customersPassed.add(customerPass2);

        independantReturned.add(customerReturn1);
        independantReturned.add(customerReturn2);
        independantReturned.add(customerReturn3);

        healthCenterReturned.add(hcReturned1);
        healthCenterReturned.add(hcReturned2);

    }


    @Test
    public void testOnFailure() throws Exception {
        Mockito.doNothing().when(fragment).callRestFailed();
        CallBackGetOtherCustomers cb = new CallBackGetOtherCustomers(fragment,list,customersPassed);
        ResponseRestCustomer response = new ResponseRestCustomer();
        response.setHealthCenters(healthCenterReturned);
        response.setIndependants(independantReturned);
        cb.onFailure(null);
        verify(fragment, Mockito.times(1)).callRestFailed();
    }


    @Test
    public void testOnResponseOkSiretDifferent() throws Exception {
        hc1.setSiretNumber(1);
        hc2.setSiretNumber(2);
        hc3.setSiretNumber(3);
        hc4.setSiretNumber(4);
        hcReturned1.setSiretNumber(5);
        hcReturned2.setSiretNumber(6);
        Mockito.doNothing().when(fragment).initAdapter(Mockito.anyList());
        Mockito.doNothing().when(fragment).isCampaignAlreadySet();
        CallBackGetOtherCustomers cb = new CallBackGetOtherCustomers(fragment,list,customersPassed);

        ResponseRestCustomer response = new ResponseRestCustomer();
        response.setIndependants(independantReturned);
        response.setHealthCenters(healthCenterReturned);
        Response responseMocked = Response.success(response);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.github.com/").build();
        cb.onResponse(responseMocked, retrofit);
        verify(fragment,Mockito.times(1)).initAdapter(customersPassed);
        verify(fragment,Mockito.times(1)).isCampaignAlreadySet();
        assertEquals(7,customersPassed.size());

    }


    @Test
    public void testtestOnResponseOkSiretSame() throws Exception {
        hc1.setSiretNumber(1);
        hc2.setSiretNumber(2);
        hc3.setSiretNumber(3);
        hc4.setSiretNumber(4);
        hcReturned1.setSiretNumber(4);
        hcReturned2.setSiretNumber(6);
        Mockito.doNothing().when(fragment).initAdapter(Mockito.anyList());
        Mockito.doNothing().when(fragment).isCampaignAlreadySet();
        CallBackGetOtherCustomers cb = new CallBackGetOtherCustomers(fragment,list,customersPassed);

        ResponseRestCustomer response = new ResponseRestCustomer();
        response.setIndependants(independantReturned);
        response.setHealthCenters(healthCenterReturned);
        Response responseMocked = Response.success(response);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.github.com/").build();
        cb.onResponse(responseMocked, retrofit);
        verify(fragment,Mockito.times(1)).initAdapter(customersPassed);
        verify(fragment,Mockito.times(1)).isCampaignAlreadySet();
        assertEquals(6,customersPassed.size());
    }

    @Test
    public void testOnResponseNok() throws Exception {
        Mockito.doNothing().when(fragment).restartCampaign(Mockito.anyList());
        CallBackGetOtherCustomers cb = new CallBackGetOtherCustomers(fragment,list,customersPassed);
        cb = spy(cb);
        Mockito.doNothing().when(cb).onFailure(null);
        ResponseRestPhoningCampaign response = new ResponseRestPhoningCampaign();
        ResponseBody responseBody = ResponseBody.create(MediaType.parse("500"),"test");
        Response responseMocked = Response.error(500, responseBody );
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.github.com/").build();
        cb.onResponse(responseMocked, retrofit);
        verify(cb,Mockito.times(1)).onFailure(null);
    }
}
