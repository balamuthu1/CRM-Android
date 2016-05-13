package fr.pds.isintheair.crmtab.jbide.uc.registercall;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import fr.pds.isintheair.crmtab.Constant;
import fr.pds.isintheair.crmtab.R;
import fr.pds.isintheair.crmtab.model.entity.Contact;
import fr.pds.isintheair.crmtab.model.rest.service.ContactRetrofitService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by jbide on 10/05/2016.
 */
public class ControllerContact {

    public static void registerContact(Contact cra, final Activity context) {


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

        ContactRetrofitService service = retrofit.create(ContactRetrofitService.class);
        Call<Boolean>          call    = service.addContacts(cra);

        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Response<Boolean> response, Retrofit retrofit) {
                if (response.isSuccess()) {

                    Log.v("ok", "Contact enregistre");
                    AlertDialog alertDialog = new AlertDialog.Builder(
                            context).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("Nouveau contact");

                    // Setting Dialog Message
                    alertDialog.setMessage("Contact enregistré");

                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.tick1);

                    // Setting OK Button
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog closed
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();


                }
                else {
                    //request not successful (like 400,401,403 etc)
                    //Handle errors
                    Log.v("rest", "no rep" + response.message());
                    AlertDialog alertDialog = new AlertDialog.Builder(
                            context).create();

                    // Setting Dialog Title
                    alertDialog.setTitle("Statut Contact");

                    // Setting Dialog Message
                    alertDialog.setMessage("Contact enregistré localement");

                    // Setting Icon to Dialog
                    alertDialog.setIcon(R.drawable.no_tick);

                    // Setting OK Button
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog closed
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();

                }
                Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable t) {
                //  Toast.makeText(getActivity(), "Request Failed", Toast.LENGTH_LONG).show();
                Log.v("Failure", "msg = " + t.getMessage());

                AlertDialog alertDialog = new AlertDialog.Builder(
                        context).create();

                // Setting Dialog Title
                alertDialog.setTitle("Statut Contact");

                // Setting Dialog Message
                alertDialog.setMessage("Contact enregistré localement : Serveur injoignable");
                // Setting Icon to Dialog
                alertDialog.setIcon(R.drawable.no_tick);

                // Setting OK Button
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed
                    }
                });

                // Showing Alert Message
                alertDialog.show();
            }
        });


    }


}
