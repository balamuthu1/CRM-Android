package fr.pds.isintheair.crmtab.model.rest.service;

import fr.pds.isintheair.crmtab.model.entity.User;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.POST;

/**
 * Created by jbide on 22/01/2016.
 */
public interface LoginService {
    @POST("login")
    Call<User> login(@Body User user);
}
