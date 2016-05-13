package fr.pds.isintheair.crmtab;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import fr.pds.isintheair.crmtab.controller.message.CallBackSaveUnfinishedPhoningCampaign;
import fr.pds.isintheair.crmtab.model.entity.ResponseRestPhoningCampaign;
import fr.pds.isintheair.crmtab.view.fragment.CreatePhoningCampaignFragment;
import retrofit.Response;
import retrofit.Retrofit;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * Created by tlacouque on 12/04/2016.
 */
public class CallBackSaveUnfinishedPhoningCampaignTest {

    CreatePhoningCampaignFragment fragment;
    CallBackSaveUnfinishedPhoningCampaign cb;

    @Before
    public void setUp() throws Exception {
        fragment =  Mockito.mock(CreatePhoningCampaignFragment.class);
        cb = new CallBackSaveUnfinishedPhoningCampaign(fragment);
    }

    @Test
    public void testStopCampaignTrue() throws Exception {
        ResponseRestPhoningCampaign response = new ResponseRestPhoningCampaign();
        response.setSaved(true);
        Response responseMocked = Response.success(response);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.github.com/").build();
        Mockito.doNothing().when(fragment).stopCampaign(true);
        cb.onResponse(responseMocked,retrofit);
        verify(fragment, Mockito.times(1)).stopCampaign(true);
    }

    @Test
    public void testStopCampaignFalse() throws Exception {
        ResponseRestPhoningCampaign response = new ResponseRestPhoningCampaign();
        response.setSaved(false);
        Response responseMocked = Response.success(response);
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.github.com/").build();
        Mockito.doNothing().when(fragment).stopCampaign(false);
        cb.onResponse(responseMocked,retrofit);
        verify(fragment, Mockito.times(1)).stopCampaign(false);
    }
}
