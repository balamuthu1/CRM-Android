package fr.pds.isintheair.crmtab.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import fr.pds.isintheair.crmtab.R;
import fr.pds.isintheair.crmtab.controller.service.CalendarService;
import fr.pds.isintheair.crmtab.controller.service.CallService;
import fr.pds.isintheair.crmtab.controller.service.ContactService;
import fr.pds.isintheair.crmtab.controller.service.ListennerCallEndedEvent;
import fr.pds.isintheair.crmtab.ctruong.uc.propsect.suggestion.notification.service.NotificationIntentService;
import fr.pds.isintheair.crmtab.helper.CredentialHelper;
import fr.pds.isintheair.crmtab.model.dao.UserDAO;
import fr.pds.isintheair.crmtab.model.entity.User;
import fr.pds.isintheair.crmtab.model.rest.RetrofitHandlerSingleton;
import fr.pds.isintheair.crmtab.model.rest.service.LoginService;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


/**
 * Created by jbide on 22/01/2016.
 */

/**
 * Fixed by jbide on 26/01/2016 : 17:00
 */
public class LoginActivity extends Activity implements Callback<User> {
    @Bind(R.id.login_edittext)
    EditText loginEditText;

    @Bind(R.id.loginerror)
    TextView error;

    @Bind(R.id.password_edittext)
    EditText passwordEditText;

    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    @Bind(R.id.connection_button)
    Button connect;

    private User currentUser;

    @OnClick(R.id.connection_button)
    public void onConnectionClick() {
        progressBar.setVisibility(View.VISIBLE);
        error.setVisibility(View.GONE);

        String login    = loginEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String basic    = CredentialHelper.getBase64Credentials(login, password);

        currentUser.setEmail(login);
        currentUser.setPassword(basic);

        LoginService loginService = RetrofitHandlerSingleton.getInstance().getLoginService();
        Call<User>   call         = loginService.login(currentUser);

        call.enqueue(LoginActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        loginEditText.setText("test@crm.fr");
        passwordEditText.setText("password");
        progressBar.setVisibility(View.GONE);
        error.setVisibility(View.GONE);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConnectionClick();
            }
        });

        currentUser = UserDAO.getCurrentUser();


        if (currentUser != null) {
            initService();
            startActivity(new Intent(this, MainActivity.class));
        }
        else {
            currentUser = new User();
        }
    }

    @Override
    public void onResponse(Response<User> response, Retrofit retrofit) {
        if (response.isSuccess()) {
            currentUser = response.body();
            Log.v("rep", response.body().toString());

            if (currentUser == null) {
                currentUser = new User();

                currentUser.setEmail("test@crm.fr");
                currentUser.setId("bd299fa2-244c-4b6b-9966-49a84192cc8c");
                currentUser.setFname("testFname");
                currentUser.setLname("testLname");
                currentUser.setPassword("password");
                currentUser.setTel("0762506058");
            }

            currentUser.save();
            initService();

            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }

    @Override
    public void onFailure(Throwable t) {
        currentUser.delete();
        error.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    private void initService() {
        startService(new Intent(LoginActivity.this, CallService.class));
        startService(new Intent(LoginActivity.this, CalendarService.class));
        startService(new Intent(LoginActivity.this, NotificationIntentService.class));
        startService(new Intent(LoginActivity.this, ListennerCallEndedEvent.class));
        startService(new Intent(LoginActivity.this, ContactService.class));
    }
}