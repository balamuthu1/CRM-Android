package fr.pds.isintheair.crmtab.view.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import fr.pds.isintheair.crmtab.R;
import fr.pds.isintheair.crmtab.helper.FbDBHelper;
import fr.pds.isintheair.crmtab.model.entity.FbEventsPojo.Data;
import fr.pds.isintheair.crmtab.model.entity.FbEventsPojo.FbEvent;

public class EventSubscriptionActivity extends Activity {

    private LoginButton loginBtn;
    private String USER_ID;
    private CallbackManager callbackManager;
    private TextView userName;
    private AccessToken accessToken;
    private GraphRequest graphRequest;
    private static String FbEventJson;
    private NotificationManager myNotificationManager;
    private int notificationIdOne = 111;
    Gson gson = new Gson();
    Data data[];
    ArrayList<String> eventArray = new ArrayList<>();
    FbDBHelper mydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_event_subscription);
        userName = (TextView) findViewById(R.id.user_name);
        loginBtn = (LoginButton) findViewById(R.id.fb_login_button);
        if (loginBtn != null) {
            loginBtn.setReadPermissions("user_events");
        }
        mydb = new FbDBHelper(this);


        loginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                accessToken = loginResult.getAccessToken();
                USER_ID = loginResult.getAccessToken().getUserId();

                 /* make the API call to get user information*/
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    userName.setText("Bonjour " + object.getString("name") + ", vous êtes maintenant souscrit aux évenements sur FACEBOOK :)");
                                } catch(JSONException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();


                /* make the API call to get events from Groups*/
                graphRequest = new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/1046656248731710/events/",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                /* handle the result */

                                FbEvent event = gson.fromJson(response.getRawResponse(), FbEvent.class);
                                data = event.getData();
                                if(data.length > 0){
                                    for (Data d : data){
                                        if((!mydb.checkIfDataExistsInDb(d)) && (!mydb.checkIfPendingDataExistsInDb(d)))
                                            notifyUser(d);

                                    }
                                }



                                //userName.setText(response.getRawResponse() + "\n\n");
                                //Toast.makeText(MainActivity.this, response.getRawResponse(), Toast.LENGTH_SHORT).show();


                            }
                        }
                );
                new Timer().scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        graphRequest.executeAsync();

                    }
                }, 0, 3600000);//put here time 1000 milliseconds=1 second 3600000 = 1h
                //Toast.makeText(MainActivity.this, "user id: " + loginResult.getAccessToken().getUserId(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancel() {
                userName.setText("Login attempt canceled.");
            }

            @Override
            public void onError(FacebookException e) {
                userName.setText("Login attempt failed.");
            }
        });





    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void notifyUser(Data data){

        // Invoking the default notification service
        NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("Facebook Events");
        mBuilder.setContentText(data.getName());
        mBuilder.setDefaults(
                Notification.DEFAULT_SOUND
                        | Notification.DEFAULT_VIBRATE);


        mBuilder.setSmallIcon(R.drawable.com_facebook_favicon_white);
        mBuilder.setAutoCancel(true);
        // Increase notification number every time a new notification arrives
        // mBuilder.setNumber(++numMessagesOne);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, FbEventActivity.class);
        resultIntent.putExtra("notificationId", data.getId().hashCode());
        resultIntent.putExtra("data", data);
        //it is important to set different action to not to repeat the notification
        resultIntent.setAction("dummy_action_" + data.getId().hashCode());

        //This ensures that navigating backward from the Activity leads out of the app to Home page
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);


        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        // start the activity when the user clicks the notification text
        mBuilder.setContentIntent(resultPendingIntent);

        myNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // pass the Notification object to the system
        myNotificationManager.notify( data.getId().hashCode() , mBuilder.build());

    }
    public void launchTwitterActivity(View view) {
        startActivity(new Intent(this, TwitterActivity.class));
    }

    public void search(View view){
        startActivity(new Intent(this, GoingEventDetails.class));
    }

}