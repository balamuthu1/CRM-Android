package fr.pds.isintheair.crmtab.controller.message;

import fr.pds.isintheair.crmtab.model.entity.ResponseRestPhoningCampaign;
import fr.pds.isintheair.crmtab.view.fragment.CreatePhoningCampaignFragment;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by tlacouque on 12/04/2016.
 * Class used to save an unfinished phoning campaign
 */
public class CallBackSaveUnfinishedPhoningCampaign implements Callback<ResponseRestPhoningCampaign> {

    CreatePhoningCampaignFragment fragment;


    public CallBackSaveUnfinishedPhoningCampaign(CreatePhoningCampaignFragment fragment) {
        this.fragment = fragment;
    }

    /**
     * If there is no error on the response, it stop the campaign. If there is errors it called on failure method
     * @param response
     * @param retrofit
     */
    @Override
    public void onResponse(Response<ResponseRestPhoningCampaign> response, Retrofit retrofit) {
        boolean bool = false;
        if (response.errorBody() == null) {
            if(response.body().isSaved()) {
                bool = true;
            }
        }
        fragment.stopCampaign(bool);
    }

    /**
     * Called when there is an error on the rest call/response.
     * It call the method stopCampaign from the fragment pass in parameter.
     * @param t
     */
    @Override
    public void onFailure(Throwable t) {
        fragment.stopCampaign(false);
    }
}
