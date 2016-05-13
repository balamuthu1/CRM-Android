package fr.pds.isintheair.phonintheair.notifyPresence;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import fr.pds.isintheair.phonintheair.R;
import fr.pds.isintheair.phonintheair.model.constant.Constant;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by jbide on 11/05/2016.
 */
public class ClockinController {

    public static void clockin(String idtag, final Activity context) {


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

        Retrofit                gen     = new Retrofit.Builder().baseUrl(Constant.REST_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
        NotifyPresenceInterface service = gen.create(NotifyPresenceInterface.class);

        //Call<ClockinObject> call = service.clockin(new ClockinObject(UserDAO.getCurrentUser(), idtag));
        User user = new User();
        user.setId("bd299fa2-244c-4b6b-9966-49a84192cc8c");
        Call<ClockinObject> call = service.clockin(new ClockinObject(user, idtag));

        call.enqueue(new Callback<ClockinObject>() {
            @Override
            public void onResponse(Response<ClockinObject> response, Retrofit retrofit) {
                if (response.isSuccess()) {

                    if (response.body() != null) {
                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.DATE, 1);
                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                        System.out.println(cal.getTime());
                        // Output "Wed Sep 26 14:23:28 EST 2012"

                        String formatted = format1.format(cal.getTime());
                        System.out.println(formatted);
                        // Output "2012-09-26"

                        //System.out.println(format1.parse(formatted));
                        // Output "Wed Sep 26 00:00:00 EST 2012"

                        AlertDialog alertDialog = new AlertDialog.Builder(
                                context).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("Vous venez de badger");
                        ClockinObject    rep = response.body();
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        // Setting Dialog Message
                        alertDialog.setMessage("Emplacement : " + rep.getUser().getLocation() + "  " + "Heure : " + rep.getTime());

                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.drawable.sucess);

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

                        AlertDialog alertDialog = new AlertDialog.Builder(
                                context).create();

                        // Setting Dialog Title
                        alertDialog.setTitle("Tag inconnu");

                        // Setting Dialog Message
                        //alertDialog.setMessage("Emplacement :" + "Heure : ");

                        // Setting Icon to Dialog
                        alertDialog.setIcon(R.drawable.wrong);

                        // Setting OK Button
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog closed
                            }
                        });

                    }

                }
                else {
                    Toast.makeText(context, "req is not success", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(context, "Failure ", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
