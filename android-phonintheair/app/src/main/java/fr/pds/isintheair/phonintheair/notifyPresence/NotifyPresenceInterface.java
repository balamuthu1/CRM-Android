package fr.pds.isintheair.phonintheair.notifyPresence;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by jbide on 11/05/2016.
 */
public interface NotifyPresenceInterface {
    @POST("notifypresence/clockin")
    Call<ClockinObject> clockin(@Body ClockinObject clockinObject);

}
