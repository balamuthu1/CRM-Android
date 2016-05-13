package fr.pds.isintheair.crmtab.view.activity;

/**
 * Created by Muthu on 06/05/2016.
 */

import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.client.methods.HttpRequestBase;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.params.BasicHttpParams;
import fr.pds.isintheair.crmtab.R;
import fr.pds.isintheair.crmtab.model.entity.twitter.Authenticated;
import fr.pds.isintheair.crmtab.model.entity.twitter.Search;
import fr.pds.isintheair.crmtab.model.entity.twitter.SearchResults;
import fr.pds.isintheair.crmtab.model.entity.twitter.Searches;

/**
 * Demonstrates how to use a twitter application keys to search
 */
public class TwitterActivity extends ListActivity {

    private ListActivity activity;
    final static String SearchTerm = "séminaire santé";
    final static String LOG_TAG = "rnc";
    private NotificationManager myNotificationManager;
    String Key = null;
    String Secret = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        Key = getStringFromManifest("CONSUMER_KEY");
        Secret = getStringFromManifest("CONSUMER_SECRET");

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                downloadSearches();

            }
        }, 0, 3600000);//put here time 1000 milliseconds=1 second 3600000 = 1h
        startActivity(new Intent(this, EventSubscriptionActivity.class));

    }

    private String getStringFromManifest(String key) {
        String results = null;

        try {
            Context context = this.getBaseContext();
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            results = (String)ai.metaData.get(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return results;
    }

    // download twitter searches after first checking to see if there is a network connection
    public void downloadSearches() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadTwitterTask().execute(SearchTerm);
        } else {
            Log.v(LOG_TAG, "No network connection available.");
        }
    }

    // Uses an AsyncTask to download data from Twitter
    private class DownloadTwitterTask extends AsyncTask<String, Void, String> {
        final static String TwitterTokenURL = "https://api.twitter.com/oauth2/token";
        final static String TwitterSearchURL = "https://api.twitter.com/1.1/search/tweets.json?q=";

        @Override
        protected String doInBackground(String... searchTerms) {
            String result = null;

            if (searchTerms.length > 0) {
                result = getSearchStream(searchTerms[0]);
            }
            return result;
        }

        // onPostExecute convert the JSON results into a Twitter object (which is an Array list of tweets
        @Override
        protected void onPostExecute(String result) {
            Searches searches = jsonToSearches(result);

            // lets write the results to the console as well
            for (Search search : searches) {
                //Log.i(LOG_TAG, search.getText());
                notifyUser(search);

            }

            // send the tweets to the adapter for rendering
           // ArrayAdapter<Search> adapter = new ArrayAdapter<Search>(activity, android.R.layout.simple_list_item_1, searches);
            //setListAdapter(adapter);
        }

        // converts a string of JSON data into a SearchResults object
        private Searches jsonToSearches(String result) {
            Searches searches = null;
            if (result != null && result.length() > 0) {
                try {
                    Gson gson = new Gson();
                    // bring back the entire search object
                    SearchResults sr = gson.fromJson(result, SearchResults.class);
                    // but only pass the list of tweets found (called statuses)
                    searches = sr.getStatuses();
                } catch (IllegalStateException ex) {
                    // just eat the exception for now, but you'll need to add some handling here
                }
            }
            return searches;
        }

        // convert a JSON authentication object into an Authenticated object
        private Authenticated jsonToAuthenticated(String rawAuthorization) {
            Authenticated auth = null;
            if (rawAuthorization != null && rawAuthorization.length() > 0) {
                try {
                    Gson gson = new Gson();
                    auth = gson.fromJson(rawAuthorization, Authenticated.class);
                } catch (IllegalStateException ex) {
                    // just eat the exception for now, but you'll need to add some handling here
                }
            }
            return auth;
        }

        private String getResponseBody(HttpRequestBase request) {
            StringBuilder sb = new StringBuilder();
            try {

                DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
                HttpResponse response = httpClient.execute(request);
                int statusCode = response.getStatusLine().getStatusCode();
                String reason = response.getStatusLine().getReasonPhrase();

                if (statusCode == 200) {

                    HttpEntity entity = response.getEntity();
                    InputStream inputStream = entity.getContent();

                    BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    String line = null;
                    while ((line = bReader.readLine()) != null) {
                        sb.append(line);
                    }
                } else {
                    sb.append(reason);
                }
            } catch (UnsupportedEncodingException ex) {
            } catch (ClientProtocolException ex1) {
            } catch (IOException ex2) {
            }
            return sb.toString();
        }

        private String getStream(String url) {
            String results = null;

            // Step 1: Encode consumer key and secret
            try {
                // URL encode the consumer key and secret
                String urlApiKey = URLEncoder.encode(Key, "UTF-8");
                String urlApiSecret = URLEncoder.encode(Secret, "UTF-8");

                // Concatenate the encoded consumer key, a colon character, and the encoded consumer secret
                String combined = urlApiKey + ":" + urlApiSecret;

                // Base64 encode the string
                String base64Encoded = Base64.encodeToString(combined.getBytes(), Base64.NO_WRAP);

                // Step 2: Obtain a bearer token
                HttpPost httpPost = new HttpPost(TwitterTokenURL);
                httpPost.setHeader("Authorization", "Basic " + base64Encoded);
                httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
                httpPost.setEntity(new StringEntity("grant_type=client_credentials"));
                String rawAuthorization = getResponseBody(httpPost);
                Authenticated auth = jsonToAuthenticated(rawAuthorization);

                // Applications should verify that the value associated with the
                // token_type key of the returned object is bearer
                if (auth != null && auth.token_type.equals("bearer")) {

                    // Step 3: Authenticate API requests with bearer token
                    HttpGet httpGet = new HttpGet(url);

                    // construct a normal HTTPS request and include an Authorization
                    // header with the value of Bearer <>
                    httpGet.setHeader("Authorization", "Bearer " + auth.access_token);
                    httpGet.setHeader("Content-Type", "application/json");
                    // update the results with the body of the response
                    results = getResponseBody(httpGet);
                }
            } catch (UnsupportedEncodingException ex) {
            } catch (IllegalStateException ex1) {
            }
            return results;
        }

        private String getSearchStream(String searchTerm) {
            String results = null;
            try {
                String encodedUrl = URLEncoder.encode(searchTerm, "UTF-8");
                results = getStream(TwitterSearchURL + encodedUrl + "&result_type=recent");
            } catch (UnsupportedEncodingException ex) {
            } catch (IllegalStateException ex1) {
            }
            return results;
        }
    }
    public void notifyUser(Search search){

        // Invoking the default notification service
        NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("Twitter Events");
        mBuilder.setContentText(search.getText());
        mBuilder.setDefaults(
                Notification.DEFAULT_SOUND
                        | Notification.DEFAULT_VIBRATE);


        mBuilder.setSmallIcon(R.drawable.twitter);
        mBuilder.setAutoCancel(true);
        // Increase notification number every time a new notification arrives
        // mBuilder.setNumber(++numMessagesOne);

        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, FbEventActivity.class);
        resultIntent.putExtra("notificationId", Long.toString(search.getId()).hashCode());
        resultIntent.putExtra("tweet",(new Gson()).toJson(search) );
        //it is important to set different action to not to repeat the notification
        resultIntent.setAction("dummy_action_" + Long.toString(search.getId()).hashCode());

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
        myNotificationManager.notify( Long.toString(search.getId()).hashCode() , mBuilder.build());

    }
}
