package fr.pds.isintheair.crmtab.model.rest.service;

import java.util.List;

import fr.pds.isintheair.crmtab.model.entity.Contact;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by Muthu on 30/12/2015.
 */
public interface ContactRetrofitService {
    @POST("contact/addContact")
    Call<Boolean> addContacts(@Body Contact contact);


    @GET("contact/getContacts")
    Call<List<Contact>> getContactList();

}
