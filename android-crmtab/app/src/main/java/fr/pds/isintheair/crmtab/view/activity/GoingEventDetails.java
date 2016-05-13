package fr.pds.isintheair.crmtab.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import fr.pds.isintheair.crmtab.R;
import fr.pds.isintheair.crmtab.controller.adapter.EventAdapter;
import fr.pds.isintheair.crmtab.helper.FbDBHelper;
import fr.pds.isintheair.crmtab.model.entity.FbEventsPojo.Data;

public class GoingEventDetails extends Activity {

    ListView listView ;
    FbDBHelper mydb;
    EventAdapter adapter;
    AlertDialog alertDialog;
    List<Data> eventList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_going_event_details);

        mydb = new FbDBHelper(this);


        eventList = mydb.getAttendingDataInDb();
        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);



        // Define a new Adapter

        adapter = new EventAdapter(this, eventList);
        adapter.notifyDataSetChanged();

        // Assign adapter to ListView
        listView.setAdapter(adapter);
        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                showDetails(eventList.get(position));
            }

        });
    }

    public void showDetails(Data data){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title
        alertDialogBuilder.setTitle(data.getName());

        // set dialog message
        alertDialogBuilder
                .setMessage(data.getDescription()+"\n\n" + "Début: " + data.getStart_time() +"\n\n" +
                                "Fin: " +data.getEnd_time() +"\n\n" + "Place: " + data.getPlace().getName())
                .setCancelable(true)
                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        alertDialog.cancel();
                    }
                });

        // create alert dialog
        alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }
}
