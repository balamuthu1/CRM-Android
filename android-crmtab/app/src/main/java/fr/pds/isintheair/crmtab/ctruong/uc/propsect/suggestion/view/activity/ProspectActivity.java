package fr.pds.isintheair.crmtab.ctruong.uc.propsect.suggestion.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import fr.pds.isintheair.crmtab.R;
import fr.pds.isintheair.crmtab.ctruong.uc.propsect.suggestion.adapter.ProspectAdapter;
import fr.pds.isintheair.crmtab.model.rest.service.ProspectRestConfig;
import fr.pds.isintheair.crmtab.model.entity.Prospect;
import fr.pds.isintheair.crmtab.model.rest.service.ProspectRetrofitAPI;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class ProspectActivity extends Activity {

    final static String TAG                    = ProspectActivity.class.getSimpleName();
    final static String PROSPECT_NAME          = "name";
    final static String PROSPECT_FINESS        = "finess";
    final static String PROSPECT_SIRET         = "siret";
    final static String PROSPECT_STREET_NUMBER = "number";
    final static String PROSPECT_STREET_NAME   = "street";
    final static String PROSPECT_CODE          = "code";
    final static String PROSPECT_TOWN          = "town";
    final static String PROSPECT_WEBSITE       = "website";
    final static String PROSPECT_TURNOVER      = "turnover";
    final static String PROSPECT_ES_TYPE       = "type";
    final static String PROSPECT_BED           = "bed";
    @Bind(R.id.lview_prospect)
    ListView lview_prospect;
    List<Prospect>  prospects;
    ProspectAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prospect);
        ButterKnife.bind(this);

        Retrofit             retrofit = new Retrofit.Builder().baseUrl(ProspectRestConfig.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        ProspectRetrofitAPI  api      = retrofit.create(ProspectRetrofitAPI.class);
        Call<List<Prospect>> call     = api.getProspects();
        call.enqueue(new Callback<List<Prospect>>() {

            @Override
            public void onResponse(Response<List<Prospect>> response, Retrofit retrofit) {
                Log.i("abc", "onResponse: check");
                prospects = response.body();
                Log.i(TAG, "onResponse: " + prospects.toString());
                adapter = new ProspectAdapter(ProspectActivity.this, R.layout.listview_prospect, prospects);
                lview_prospect.setAdapter(adapter);
                lview_prospect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent   intent   = new Intent(getApplication(), DetailProspect.class);
                        Prospect prospect = prospects.get(position);
                        intent.putExtra(PROSPECT_NAME, prospect.getName());
                        intent.putExtra(PROSPECT_FINESS, prospect.getFinessNumber());
                        intent.putExtra(PROSPECT_SIRET, prospect.getSiretNumber());
                        intent.putExtra(PROSPECT_STREET_NUMBER, prospect.getStreetNumber());
                        intent.putExtra(PROSPECT_STREET_NAME, prospect.getStreetName());
                        intent.putExtra(PROSPECT_CODE, prospect.getZipCode());
                        intent.putExtra(PROSPECT_TOWN, prospect.getTown());
                        intent.putExtra(PROSPECT_WEBSITE, prospect.getWebsite());
                        intent.putExtra(PROSPECT_TURNOVER, prospect.getTurnover());
                        intent.putExtra(PROSPECT_ES_TYPE, prospect.getEtablishmentType());
                        intent.putExtra(PROSPECT_BED, prospect.getBedNumber());
                        startActivityForResult(intent, 0);
                        Log.i(TAG, "onItemClick: checked");
                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i("abc", "onResponse: uncheck" + t.getMessage());
            }
        });
    }

}


