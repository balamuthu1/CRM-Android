package fr.pds.isintheair.crmtab.view.activity;

import android.app.Activity;
import android.os.Bundle;

import fr.pds.isintheair.crmtab.model.ClockinObject;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by jbide on 29/03/2016.
 */
public class NotifyPresenceActivity extends Activity implements Callback<ClockinObject> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onResponse(Response<ClockinObject> response, Retrofit retrofit) {

    }

    @Override
    public void onFailure(Throwable t) {

    }
}
