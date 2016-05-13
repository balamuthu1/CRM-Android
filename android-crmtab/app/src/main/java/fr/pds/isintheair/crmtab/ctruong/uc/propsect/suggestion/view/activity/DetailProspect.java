package fr.pds.isintheair.crmtab.ctruong.uc.propsect.suggestion.view.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketConnectionHandler;
import de.tavendo.autobahn.WebSocketException;
import fr.pds.isintheair.crmtab.R;
import fr.pds.isintheair.crmtab.model.rest.service.ProspectRestConfig;
import fr.pds.isintheair.crmtab.model.websocket.WebsocketConfig;
import fr.pds.isintheair.crmtab.model.entity.Prospect;
import fr.pds.isintheair.crmtab.model.rest.service.ProspectRetrofitAPI;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class DetailProspect extends Activity {

    @Bind(R.id.tv_prospect_name)
    TextView tv_prospect_name;

    @Bind(R.id.tv_prospect_etablishmentType)
    TextView tv_prospect_etablishmentType;

    @Bind(R.id.tv_prospect_bed)
    TextView tv_prospect_bed;

    @Bind(R.id.tv_prospect_finess)
    TextView tv_prospect_finess;

    @Bind(R.id.tv_prospect_siret)
    TextView tv_prospect_siret;

    @Bind(R.id.tv_prospect_streetName)
    TextView tv_prospect_streetName;

    @Bind(R.id.tv_prospect_streetNumber)
    TextView tv_prospect_streetNumber;

    @Bind(R.id.tv_prospect_town)
    TextView tv_prospect_town;

    @Bind(R.id.tv_prospect_website)
    TextView tv_prospect_website;

    @Bind(R.id.tv_prospect_turnover)
    TextView tv_prospect_turnover;

    @Bind(R.id.tv_prospect_zipcode)
    TextView tv_prospect_zipcode;

    @Bind(R.id.btn_prospect_insert)
    Button btn_prospect_insert;

    private static final String TAG = DetailProspect.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_prospect);
        ButterKnife.bind(this);
        final Intent intent = getIntent();
        tv_prospect_name.setText(intent.getStringExtra(ProspectActivity.PROSPECT_NAME));
        tv_prospect_etablishmentType.setText(intent.getStringExtra(ProspectActivity.PROSPECT_ES_TYPE));
        tv_prospect_bed.setText(String.valueOf(intent.getIntExtra(ProspectActivity.PROSPECT_BED, 0)));
        tv_prospect_streetNumber.setText(String.valueOf(intent.getIntExtra(ProspectActivity.PROSPECT_STREET_NUMBER, 0)));
        tv_prospect_zipcode.setText(String.valueOf(intent.getIntExtra(ProspectActivity.PROSPECT_CODE, 0)));
        tv_prospect_siret.setText(String.valueOf(intent.getLongExtra(ProspectActivity.PROSPECT_SIRET, 0)));
        tv_prospect_finess.setText(String.valueOf(intent.getLongExtra(ProspectActivity.PROSPECT_FINESS, 0)));
        tv_prospect_turnover.setText(String.valueOf(intent.getLongExtra(ProspectActivity.PROSPECT_TURNOVER, 0)));
        tv_prospect_streetName.setText(intent.getStringExtra(ProspectActivity.PROSPECT_STREET_NAME));
        tv_prospect_town.setText(intent.getStringExtra(ProspectActivity.PROSPECT_TOWN));
        tv_prospect_website.setText(intent.getStringExtra(ProspectActivity.PROSPECT_WEBSITE));

        btn_prospect_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl(ProspectRestConfig.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
                ProspectRetrofitAPI api = retrofit.create(ProspectRetrofitAPI.class);
                Call<Prospect> call = api.createClient(intent.getLongExtra(ProspectActivity.PROSPECT_SIRET, 0));
                call.enqueue(new Callback<Prospect>() {
                    @Override
                    public void onResponse(Response<Prospect> response, Retrofit retrofit) {
                        Log.i(TAG, "onResponse: checked");
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.i(TAG, "onFailure: checked" + t.getMessage());
                    }
                });
                Intent intent1 = new Intent(DetailProspect.this, ProspectActivity.class);
                startActivity(intent1);
                start(intent.getStringExtra(ProspectActivity.PROSPECT_NAME));
            }
        });
    }

    public void backToActivity(View view) {
        Intent intent = new Intent(DetailProspect.this, ProspectActivity.class);
        startActivity(intent);
    }

    private void pushNotif(String message){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Prospect").setSmallIcon(R.drawable.logo).setContentText("New client " + message + "was inserted inside the client list.").setAutoCancel(true).setVibrate(new long[]{1000, 1000, 1000, 1000, 1000}).setLights(Color.RED, 3000, 3000);
        Intent intent = new Intent(this, DetailProspect.class);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addParentStack(DetailProspect.class);
        taskStackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = taskStackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //notificationID allows you to update the notification later on.
        mNotificationManager.notify(1, builder.build());
    }

    private final WebSocketConnection connection = new WebSocketConnection();

    private void start(final String message){
        try {
            connection.connect(WebsocketConfig.WSURI, new WebSocketConnectionHandler(){
                @Override
                public void onOpen() {
                    super.onOpen();
                    Log.i(TAG, "I'm connect with the server");

                    connection.sendTextMessage(message);
                }

                @Override
                public void onClose(int code, String reason) {
                    super.onClose(code, reason);
                }

                @Override
                public void onTextMessage(String payload) {
                    super.onTextMessage(payload);
                    pushNotif(message);
                }
            });
        } catch (WebSocketException e) {
            e.printStackTrace();
        }
    }

}
