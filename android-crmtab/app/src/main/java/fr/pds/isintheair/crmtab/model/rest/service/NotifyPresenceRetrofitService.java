package fr.pds.isintheair.crmtab.model.rest.service;

import fr.pds.isintheair.crmtab.model.ClockinObject;
import fr.pds.isintheair.crmtab.model.entity.Tag;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by jbide on 29/03/2016.
 */
public interface NotifyPresenceRetrofitService {

    @POST("notifypresence/clockin")
    Call<ClockinObject> clockin(@Body ClockinObject clockinObject);

    @POST("notifypresence/addnewtag")
    Call<Boolean> addNewTag(@Body Tag tag);

}
