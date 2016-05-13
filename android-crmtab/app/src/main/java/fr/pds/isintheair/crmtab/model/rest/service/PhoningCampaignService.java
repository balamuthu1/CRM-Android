package fr.pds.isintheair.crmtab.model.rest.service;

import fr.pds.isintheair.crmtab.Constant;
import fr.pds.isintheair.crmtab.model.entity.MessageRestPhoningCampaign;
import fr.pds.isintheair.crmtab.model.entity.ResponseRestPhoningCampaign;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by tlacouque on 27/03/2016.
 * Interface used to contact rest service for phoning campaign
 */
public interface PhoningCampaignService {

    static String BASE_URL = Constant.REST_URL;

    // static String BEGIN_URL = "/SpringRESTapi";

    /**
     * Call rest used to demand contacts of the customer pass with messageRestPhoningCampaign object
     *
     * @param message
     * @return
     */
    @POST("/api/phoningcampaign/contact")
    Call<ResponseRestPhoningCampaign> getContacts(@Body MessageRestPhoningCampaign message);

    /**
     * Call rest used to save the current phoning campaign pass with messageRestPhoningCampaign object
     *
     * @param message
     * @return
     */
    @POST("/api/phoningcampaign")
    Call<ResponseRestPhoningCampaign> savePhoningCampaign(@Body MessageRestPhoningCampaign message);
}
