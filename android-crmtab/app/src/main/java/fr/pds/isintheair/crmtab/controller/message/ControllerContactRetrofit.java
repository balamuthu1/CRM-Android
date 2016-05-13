package fr.pds.isintheair.crmtab.controller.message;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.pds.isintheair.crmtab.Constant;
import fr.pds.isintheair.crmtab.model.entity.Contact;
import fr.pds.isintheair.crmtab.model.rest.service.ContactRetrofitService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Muthu on 02/03/2016.
 */
public class ControllerContactRetrofit {

    public Boolean addContacts(Contact contact, final Context context) {

        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.REST_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //retrofit.client().setConnectTimeout(5000, TimeUnit.MILLISECONDS);
        ContactRetrofitService iContactRetrofitService = retrofit.create(ContactRetrofitService.class);
        Call<Boolean>          call                    = iContactRetrofitService.addContacts(contact);
        call.enqueue(new Callback<Boolean>() {

            @Override
            public void onResponse(Response<Boolean> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    //Toast.makeText(context, "En cours d'import...", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, response.message(), Toast.LENGTH_LONG).show();
                }
                Toast.makeText(context, "Success !", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return false;
    }
}
