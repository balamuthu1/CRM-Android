package fr.pds.isintheair.crmtab.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.pds.isintheair.crmtab.R;
import fr.pds.isintheair.crmtab.helper.SharedPreferenceHelper;
import fr.pds.isintheair.crmtab.model.rest.RetrofitHandlerSingleton;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class AdminActivity extends Activity {

    @Bind(R.id.file_id)
    TextView fileIdTextView;

    @Bind(R.id.last_dump_date)
    TextView lastDumpDateTextView;

    @Bind(R.id.last_restore_date)
    TextView lastRestoreDateTextView;

    @OnClick(R.id.dump_button)
    public void onDumpButtonClick() {
        Call<String> call = RetrofitHandlerSingleton.getInstance().getDatabaseService().dumpDatabase();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Toast.makeText(AdminActivity.this, "La base de donnée a été correctement sauvegardée", Toast.LENGTH_LONG).show();
                SharedPreferenceHelper.writeString("lastDumpFileId", response.body());
                fileIdTextView.setText("Last dump file id : " + response.body());
                lastDumpDateTextView.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE).format(Calendar.getInstance().getTime()));
            }

            @Override
            public void onFailure(Throwable throwable) {
                Toast.makeText(AdminActivity.this, "Une erreur s'est produite, veuillez réessayer plus tard", Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnClick(R.id.restore_button)
    public void onRestoreButtonClick() {
        Call<String> call = RetrofitHandlerSingleton.getInstance().getDatabaseService().restoreDatabase();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Toast.makeText(AdminActivity.this, "La base de donnée a été correctement rétablie", Toast.LENGTH_LONG).show();
                lastRestoreDateTextView.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE).format(Calendar.getInstance().getTime()));
            }

            @Override
            public void onFailure(Throwable throwable) {
                Toast.makeText(AdminActivity.this, "Une erreur s'est produite, veuillez réessayer plus tard", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        ButterKnife.bind(this);


    }
}
