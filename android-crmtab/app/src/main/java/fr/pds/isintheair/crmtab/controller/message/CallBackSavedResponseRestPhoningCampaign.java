package fr.pds.isintheair.crmtab.controller.message;

import fr.pds.isintheair.crmtab.model.entity.PhoningCampaign;
import fr.pds.isintheair.crmtab.model.entity.ResponseRestPhoningCampaign;
import fr.pds.isintheair.crmtab.view.fragment.CallPhoningCampaignFragment;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by tlacouque on 08/04/2016.
 */
public class CallBackSavedResponseRestPhoningCampaign implements Callback<ResponseRestPhoningCampaign> {
    PhoningCampaignController controller;


    public CallBackSavedResponseRestPhoningCampaign() {
    }

    public CallBackSavedResponseRestPhoningCampaign(PhoningCampaignController controller) {
        this.controller = controller;

    }

    /**
     * Called when a good HTTP response is return
     * @param response
     * @param retrofit
     */
    @Override
    public void onResponse(Response<ResponseRestPhoningCampaign> response, Retrofit retrofit) {

        if (response.errorBody() == null) {
            if(response.body().isSaved()) {
                controller.setBoolReponse(true);
            }
        }
        controller.getFragment().endCampaign(controller.isBoolReponse());

    }

    /**
     * Called when a bad HTTP response is return
     * @param t
     */
    @Override
    public void onFailure(Throwable t) {
        controller.getFragment().endCampaign(controller.isBoolReponse());
    }

}
