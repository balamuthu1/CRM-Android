package fr.pds.isintheair.crmtab.controller.message;

import fr.pds.isintheair.crmtab.model.entity.ResponseRestPhoningCampaign;
import fr.pds.isintheair.crmtab.view.fragment.CreatePhoningCampaignFragment;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by tlacouque on 26/04/2016.
 * Class used to get all the contact used in a phoning campaign
 */
public class CallBackGetContactsForPhoningCampaign implements Callback<ResponseRestPhoningCampaign> {

    CreatePhoningCampaignFragment fragment;

    public CallBackGetContactsForPhoningCampaign() {
    }

    public CallBackGetContactsForPhoningCampaign(CreatePhoningCampaignFragment fragment) {
        this.fragment = fragment;
    }

    /**
     * If there is no error on the response, it restart the campaign. If there is errors it called on failure method
     * @param response
     * @param retrofit
     */
    @Override
    public void onResponse(Response<ResponseRestPhoningCampaign> response, Retrofit retrofit) {
        if (response.errorBody() == null) {
            if (response.body() != null) {
                fragment.restartCampaign(response.body().getContacts());

            }

        } else {
            onFailure(null);
        }
    }

    /**
     * Called when there is an error on the rest call/response.
     * It call the method callRestFailed from the fragment pass in parameter.
     * @param t
     */
    @Override
    public void onFailure(Throwable t) {
        fragment.callRestFailed();
    }
}
