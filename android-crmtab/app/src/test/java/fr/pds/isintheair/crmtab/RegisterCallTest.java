package fr.pds.isintheair.crmtab;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import fr.pds.isintheair.crmtab.jbide.uc.registercall.Rest.Model.Cra;
import fr.pds.isintheair.crmtab.jbide.uc.registercall.Views.displaycalls.CallLogRecyclerViewAdapter;
import fr.pds.isintheair.crmtab.model.rest.service.RegisterCallSerciceGenerator;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import static org.junit.Assert.assertTrue;

/**
 * Created by jbide on 02/03/2016.
 */
public class RegisterCallTest {

    @Test
    public void testDisplayCallLogFragment() {

        List<Cra>                  listecra = new ArrayList<Cra>();
        CallLogRecyclerViewAdapter adapter  = new CallLogRecyclerViewAdapter(listecra);

        Gson gson = new GsonBuilder().create();

        OkHttpClient httpClient = new OkHttpClient();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.REST_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .build();

        RegisterCallSerciceGenerator service = retrofit.create(RegisterCallSerciceGenerator.class);
        Call<List<Cra>>              call    = service.listcraforuser("1");
        call.enqueue(new Callback<List<Cra>>() {
            @Override
            public void onResponse(Response<List<Cra>> response, Retrofit retrofit) {
                assertTrue(response.isSuccess());
                //Toast.makeText(context, "status code" + response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Test
    public void testaddlog() {
        //Interceptor
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient();
        // add logging as last interceptor
        httpClient.interceptors().add(logging);

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.REST_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .build();

        RegisterCallSerciceGenerator service = retrofit.create(RegisterCallSerciceGenerator.class);
        Call<Boolean>                call    = service.createcra(new Cra());

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Response<Boolean> response, Retrofit retrofit) {
                assertTrue(response.isSuccess());

            }

            @Override
            public void onFailure(Throwable t) {

            }

        });
    }
}