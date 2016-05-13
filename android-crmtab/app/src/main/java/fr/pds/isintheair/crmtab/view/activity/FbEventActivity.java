package fr.pds.isintheair.crmtab.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import fr.pds.isintheair.crmtab.R;
import fr.pds.isintheair.crmtab.helper.FbDBHelper;
import fr.pds.isintheair.crmtab.model.entity.Event;
import fr.pds.isintheair.crmtab.model.entity.FbEventsPojo.Data;
import fr.pds.isintheair.crmtab.model.entity.twitter.Search;

public class FbEventActivity extends Activity {
    Data data;
    Search search;
    FbDBHelper mydb;
    String description, hdebut, hfin, lieu, title;
    Gson gson = new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fb_event);
        data = (Data) getIntent().getSerializableExtra("data");
        search = (new Gson()).fromJson((String) getIntent().getSerializableExtra("tweet"), Search.class) ;
        mydb = new FbDBHelper(this);


       if(data != null){
           try {
               if(data.getName() != null){
                   title = data.getName();
               }else{
                   title = " - ";
               }

               if(data.getDescription() != null){
                   description = data.getDescription();
               }else{
                   description = " - ";
               }

               if(data.getStart_time() != null){
                   hdebut = data.getStart_time();
               }else{
                   hdebut = " - ";
               }

               if(data.getEnd_time() != null){
                   hfin = data.getEnd_time();
               }else{
                   hfin = " - ";
               }

               if(data.getPlace() != null){
                   lieu = data.getPlace().getName();
               }else{
                   lieu = " - ";
               }
           } catch (Exception e) {
               e.printStackTrace();
           }
           showDetail(data);
       }
        if(search != null){
            showDetail(search);
        }

    }

    public void showDetail(final Data fbData) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title
        alertDialogBuilder.setTitle("Voulez vous ajouter l'évenement " + fbData.getName() +" dans votre agenda?");
        // set dialog message
        alertDialogBuilder
                .setMessage("Description: " + description+"\n"
                        + "Heure début: " + hdebut + "\n"
                        + "Heure fin: " + hfin + "\n"
                        + "Lieu: " + lieu)

                .setCancelable(false)
                .setPositiveButton("Oui",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        mydb.insertEvent(gson.toJson(fbData,Data.class));
                        try {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.FRANCE);

                            Date dateStart = sdf.parse(hdebut);
                            Date dateEnd = sdf.parse(hfin);


                            Calendar calStart = sdf.getCalendar();
                            calStart.setTime(dateStart);
                            Calendar calEnd = sdf.getCalendar();
                            calEnd.setTime(dateEnd);

                            Event event = new Event();
                            event.setTitle(title);
                            event.setStartTime(calStart);
                            event.setEndTime(calEnd);

                            event.save();


                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        FbEventActivity.this.finish();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        mydb.insertPendingEvent(gson.toJson(fbData,Data.class));
                        FbEventActivity.this.finish();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void showDetail(final Search search){
        {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    this);

            // set title
            alertDialogBuilder.setTitle("Voulez vous ajouter l'évenement posté par ' " + search.getUser().getName() +" 'dans votre agenda?");
            // set dialog message
            alertDialogBuilder
                    .setMessage("Description: " + search.getText())


                    .setCancelable(false)
                    .setPositiveButton("Oui",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, close
                            // current activity
                           // mydb.insertEvent(gson.toJson(search,Search.class));
                            getUrlFromString(search.getText());


                            FbEventActivity.this.finish();
                        }
                    })
                    .setNegativeButton("No",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, close
                            // current activity
                            mydb.insertPendingEvent(gson.toJson(search,Search.class));
                            FbEventActivity.this.finish();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }
    }

    public void getUrlFromString(String text) {
        // Pattern for recognizing a URL, based off RFC 3986
        Pattern urlPattern = Pattern.compile(
                "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                        + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                        + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

        // separate input by spaces ( URLs don't have spaces )
        String [] parts = text.split("\\s+");

        // get every part
        for( String item : parts ) {
            if(urlPattern.matcher(item).matches()) {
                //it's a good url

                Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(item));
                startActivity(intent);

            }
        }
    }

}
